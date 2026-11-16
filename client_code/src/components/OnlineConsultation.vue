<template>
 <el-drawer v-model="visible" title="在线咨询 · 花海摄影" class="studio-consultation" direction="rtl" size="min(480px, 100vw)" append-to-body destroy-on-close @closed="stop">
  <div class="consult-intro"><span class="consult-dot"></span><div><strong>{{ modeLabels[mode] }}</strong><p>{{ mode==='bot' ? '常见问题即时解答，需要工作人员协助时可转人工。' : mode==='waiting' ? '已申请人工服务，客服看到后会在这里回复。您可以继续补充需求。' : '人工客服已接入，您可以在这里继续沟通。' }}</p><el-button v-if="mode==='bot'" size="small" :loading="transferring" @click="handoff">转人工</el-button><el-tag v-else size="small">{{modeLabels[mode]}}</el-tag></div></div>
  <div class="consult-topics"><button v-for="question in questions" :key="question" @click="draft=question">{{ question }}</button></div>
  <div ref="history" class="consult-history" aria-live="polite" :aria-busy="loading">
   <p v-if="loading && !messages.length" class="consult-empty">正在加载咨询记录…</p><p v-else-if="!messages.length" class="consult-empty">你好，我是花海智能助手。<br />可以咨询预约流程、风格和价格查询方式。</p>
   <article v-for="message in messages" :key="message.id" :class="['consult-message', {mine:!!message.ask}]"><span class="consult-speaker">{{ message.localPending ? '发送中…' : (message.ask ? '我' : Number(message.adminid)===0 ? '智能助手 / 系统' : '人工客服') }}<template v-if="!message.localPending"> · {{ message.addtime }}</template></span><div class="consult-bubble"><video v-if="isVideo(message.ask || message.reply)" :src="mediaUrl(message.ask || message.reply)" controls /><el-image v-else-if="isImage(message.ask || message.reply)" :src="mediaUrl(message.ask || message.reply)" :preview-src-list="[mediaUrl(message.ask || message.reply)]" fit="contain" /><span v-else>{{ message.ask || message.reply }}</span></div></article>
  </div>
  <div v-if="loadError" class="consult-error" role="alert">记录加载失败，留言内容已保留。<el-button text @click="load">重新加载</el-button></div>
  <el-input v-model="draft" type="textarea" :rows="3" maxlength="2000" show-word-limit placeholder="请输入想咨询的内容，Enter 发送，Shift + Enter 换行" @keydown="onMessageKeydown" />
  <template #footer><span class="consult-tip">消息自动更新，聊天记录持续保留。</span><el-button type="primary" :loading="sending" :disabled="!draft.trim()" @click="send">发送消息</el-button></template>
 </el-drawer>
</template>
<script setup>
import { ref, nextTick, getCurrentInstance, onBeforeUnmount, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
const context=getCurrentInstance().appContext.config.globalProperties, router=useRouter(), route=useRoute()
const visible=ref(false),draft=ref(''),messages=ref([]),history=ref(),loading=ref(false),sending=ref(false),loadError=ref(false)
const questions=['如何选择拍摄风格？','想了解服务价格','预约后可以改期吗？']
const mode=ref('bot'),transferring=ref(false),modeLabels={bot:'花海智能助手',waiting:'等待人工接待',human:'人工客服服务中'}
async function handoff(){if(transferring.value)return;transferring.value=true;try{await context.$http.post('chat/handoff');mode.value='waiting';if(!loading.value)await load()}catch{context.$toolUtil.message('转人工失败，请重试','error')}finally{transferring.value=false}}
let timer, requestSequence=0
const mediaUrl=value=>context.$config.url+value
const isImage=value=>/^file\/[^\s]+\.(png|jpe?g|gif|webp)$/i.test(value||'')
const isVideo=value=>/^file\/[^\s]+\.mp4$/i.test(value||'')
function stop(){ clearTimeout(timer); timer=undefined; ++requestSequence; loading.value=false }
async function load(){
 if(!visible.value || loading.value || sending.value) return
 clearTimeout(timer); const sequence=++requestSequence; loading.value=true
 const nearBottom=!history.value || history.value.scrollHeight-history.value.scrollTop-history.value.clientHeight<70
 try {
  const [result,state]=await Promise.all([context.$http.get('chat/list',{params:{page:1,limit:200,sort:'id',order:'desc'}}),context.$http.get('chat/session')])
  if(sequence!==requestSequence) return
  mode.value=state.data.data.mode;messages.value=[...result.data.data.list].reverse();loadError.value=false
  await nextTick();if(nearBottom && history.value)history.value.scrollTop=history.value.scrollHeight
 } catch { if(sequence===requestSequence)loadError.value=true }
 finally{if(sequence===requestSequence){loading.value=false;if(visible.value)timer=setTimeout(load,5000)}}
}
async function open(){
 if(!context.$toolUtil.storageGet('frontToken')){localStorage.setItem('toPath',router.resolve({path:route.path,query:{...route.query,consult:'1'}}).fullPath);router.push('/login');return}
 if(context.$toolUtil.storageGet('frontSessionTable')!=='yonghu'){context.$toolUtil.message('请使用用户账号发起咨询，客服请从管理后台回复','warning');return}
 if(visible.value)return
 visible.value=true;await nextTick();load()
}
function onMessageKeydown(event){
 if(event.key!=='Enter'||event.shiftKey||event.isComposing||event.keyCode===229)return
 event.preventDefault()
 if(event.repeat)return
 send()
}
async function send(){
 const text=draft.value.trim();if(!text || sending.value)return
 const pendingId=`pending-${Date.now()}-${Math.random().toString(36).slice(2,7)}`
 const pendingMessage={id:pendingId,ask:text,adminid:null,addtime:new Date().toLocaleString(),senderType:'user',localPending:true}
 stop()
 sending.value=true
 draft.value=''
 messages.value.push(pendingMessage)
 await nextTick();if(history.value)history.value.scrollTop=history.value.scrollHeight
 try {
  const response=await context.$http.post('chat/add',{ask:text})
  const result=response.data.data||{}
  if(result.mode)mode.value=result.mode
  messages.value=messages.value.map(item=>item.id===pendingId?{...item,localPending:false}:item)
  sending.value=false
  await load()
  await nextTick();if(history.value)history.value.scrollTop=history.value.scrollHeight
 } catch {
  messages.value=messages.value.filter(item=>item.id!==pendingId)
  if(!draft.value)draft.value=text
  context.$toolUtil.message(draft.value===text?'发送失败，内容已恢复到输入框，请重试':'发送失败，请稍后重试；当前草稿已保留','error')
 } finally {sending.value=false;if(visible.value&&!timer)timer=setTimeout(load,5000)}
}
onMounted(()=>{if(route.query.consult==='1'){open();const query={...route.query};delete query.consult;router.replace({path:route.path,query})}})
watch(() => route.fullPath, () => {
 if (!localStorage.getItem('frontToken')) { visible.value=false; messages.value=[]; stop() }
})
onBeforeUnmount(stop)
defineExpose({open})
</script>
<style>
.studio-consultation{--el-color-primary:#305d4c;border-radius:0}.studio-consultation .el-drawer__title{font-size:17px;color:#294a3b}.studio-consultation .el-drawer__body{padding:12px 24px 20px}.consult-intro{display:flex;gap:12px;background:#eef2eb;border-radius:8px;padding:16px}.consult-dot{width:9px;height:9px;flex:none;border-radius:50%;background:#829b73;margin-top:5px}.consult-intro strong{font-size:14px;color:#365437}.consult-intro p{font-size:12px;line-height:1.8;color:#7b8575;margin:6px 0 0}.consult-topics{display:flex;flex-wrap:wrap;gap:8px;padding:14px 0}.consult-topics button{border:1px solid #dde5d7;background:#fff;color:#667a5a;padding:7px 10px;border-radius:16px;cursor:pointer;font-size:11px}.consult-history{height:300px;overflow-y:auto;padding:12px;background:#fafbf8;border:1px solid #edf0e8;border-radius:8px;margin-bottom:16px}.consult-empty{text-align:center;font-size:13px;color:#8a9581;line-height:2;padding:62px 12px}.consult-message{display:flex;flex-direction:column;align-items:flex-start;margin:8px 0 20px}.consult-message.mine{align-items:flex-end}.consult-speaker{font-size:10px;color:#929b8c;margin-bottom:6px}.consult-bubble{max-width:85%;padding:10px 14px;border-radius:0 10px 10px;background:#fff;border:1px solid #e6eadf;color:#4a5843;font-size:13px;line-height:1.8;white-space:pre-wrap;overflow-wrap:anywhere}.mine .consult-bubble{border-radius:10px 0 10px 10px;background:#305d4c;color:#fff;border-color:#305d4c}.consult-bubble .el-image,.consult-bubble video{max-width:230px;max-height:180px}.studio-consultation .el-drawer__footer{display:flex;align-items:center;justify-content:space-between;gap:12px}.consult-tip{font-size:11px;color:#8e9785;text-align:left}.consult-error{font-size:12px;color:#ae6447;margin-bottom:10px}@media(max-width:560px){.studio-consultation .el-drawer__body{padding:10px 16px}.consult-history{height:260px}.consult-tip{max-width:180px}}
</style>

<style>.studio-consultation .el-button--primary{background:#305d4c;border-color:#305d4c;color:#fff}.studio-consultation .el-button--primary:hover{background:#244b3a;border-color:#244b3a}.studio-consultation .el-textarea__inner{border:1px solid #e0e6d9;border-radius:6px;box-shadow:none}.studio-consultation .el-textarea__inner:focus{border-color:#305d4c}.studio-consultation .el-drawer__body{padding-bottom:16px}</style>

<style>
.studio-consultation.el-drawer{height:100%;max-width:100vw;box-shadow:-12px 0 48px #193f3020;background:#fff}
.studio-consultation .el-drawer__header{margin:0;padding:22px 24px;border-bottom:1px solid #e8ede3;color:#294a3b;flex-shrink:0}
.studio-consultation .el-drawer__body{display:flex;flex-direction:column;min-height:0;padding:18px 24px 0;overflow:hidden}
.studio-consultation .consult-intro,.studio-consultation .consult-topics,.studio-consultation .consult-error,.studio-consultation .el-textarea{flex-shrink:0}
.studio-consultation .consult-history{flex:1;min-height:60px;height:auto;margin-bottom:14px;overscroll-behavior:contain}
.studio-consultation .el-drawer__footer{padding:16px 24px max(16px,env(safe-area-inset-bottom));display:flex;align-items:center;justify-content:space-between;gap:12px;flex-shrink:0;border-top:1px solid #edf0e8;margin-top:14px}
.studio-consultation .el-textarea__inner{resize:none}
@media(max-width:560px){.studio-consultation .el-drawer__header{padding:18px 16px}.studio-consultation .el-drawer__body{padding:14px 16px 0}.studio-consultation .el-drawer__footer{padding-left:16px;padding-right:16px}.studio-consultation .consult-history{height:auto;min-height:60px}}
@media(max-height:650px){.studio-consultation .consult-intro{padding:10px}.studio-consultation .consult-intro p{margin:3px 0}.studio-consultation .consult-topics{padding:8px 0}.studio-consultation .el-drawer__header{padding-top:12px;padding-bottom:12px}}
</style>
<style>@media(max-width:560px){.studio-consultation.el-drawer{width:100vw!important;max-width:100vw!important}}</style>
