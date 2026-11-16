<template>
 <section class="booking-order">
  <header class="order-heading"><div><span>BOOKING ORDER</span><h2>拍摄预约订单</h2><p>订单编号 #{{detail.id}}</p></div><el-button @click="router.push('/index/sheyingshiyuyueList?centerType=1')">返回预约列表</el-button></header>
  <el-steps :active="detail.ispay==='已支付'?3:detail.sfsh==='是'?2:1" finish-status="success" simple><el-step title="提交预约"/><el-step title="确认档期"/><el-step title="确认付款"/></el-steps>
  <div class="order-status"><el-tag :type="detail.sfsh==='否'?'danger':detail.ispay==='已支付'?'success':'warning'">{{detail.sfsh==='否'?'预约未通过':detail.ispay==='已支付'?'已付款':detail.sfsh==='是'?'预约已确认 · 待付款':'等待确认档期'}}</el-tag><p>{{detail.sfsh==='否'?'本次预约未通过，请查看回复后重新选择档期。':detail.ispay==='已支付'?'付款状态已记录，请按预约时间参加拍摄。后续安排请联系工作室确认。':detail.sfsh==='是'?'档期审核已通过，请核对拍摄日期、时段和费用，再确认订单。':'工作室或摄影师确认档期后，才会开放付款入口；当前无需付款。'}}</p></div>
  <el-alert v-if="!workflow.stage" title="等待工作室明确套餐和服务内容后再付款" type="info" :closable="false"/><p class="service-contract"><strong>{{workflow.package_name || '套餐待确认'}}</strong><br/>{{workflow.service_content || '服务内容待确认'}}<br/>履约状态：{{stageLabels[workflow.stage] || '待确认服务'}}<br/><a v-if="workflow.delivery_url" :href="workflow.delivery_url" target="_blank" rel="noopener">查看交付照片 ↗</a></p><div class="order-columns"><section><h3>拍摄安排</h3><dl><dt>摄影师</dt><dd>{{detail.sheyingxingming || '—'}}</dd><dt>拍摄日期</dt><dd>{{detail.yuyuesheyingriqi || '—'}}</dd><dt>拍摄时段</dt><dd>{{detail.yuyueshijianduan || '—'}}</dd><dt>预约人</dt><dd>{{detail.yonghuxingming || '—'}}</dd><dt>联系电话</dt><dd>{{detail.lianxidianhua || '—'}}</dd><dt>审核回复</dt><dd>{{detail.shhf || '暂无回复'}}</dd></dl></section><aside class="order-summary"><h3>费用确认</h3><span>本次预约服务费 · 全额</span><strong>¥ {{Number(detail.yuyuejiage || 0).toFixed(2)}}</strong><p>订单由花海摄影工作室统一管理。摄影师为服务提供者，此入口不向摄影师个人转账。</p><el-button v-if="centerType && workflow.stage==='confirmed' && detail.sfsh==='是' && detail.ispay!=='已支付' && btnFrontAuth('sheyingshiyuyue','支付')" type="primary" @click="payClick">核对并支付</el-button><el-tag v-else-if="detail.ispay==='已支付'" type="success">无需重复付款</el-tag><div v-if="centerType && detail.sfsh !== '否' && workflow.stage !== 'settled'" class="order-change-actions"><el-button plain @click="requestChange('reschedule')">申请改期</el-button><el-button plain type="danger" @click="requestChange('cancel')">申请取消</el-button></div><p>改期、取消或费用疑问，请通过在线咨询联系工作室。需变更时提交申请，由工作室审核处理。</p></aside></div>
  <el-dialog v-model="changeVisible" :title="changeType === 'reschedule' ? '申请改期' : '申请取消'" width="460px" append-to-body>
    <el-form label-position="top">
      <el-form-item v-if="changeType === 'reschedule'" label="新的拍摄日期和时间段">
        <div class="change-fields"><el-date-picker v-model="changeForm.date" type="date" value-format="YYYY-MM-DD" placeholder="选择日期" :disabled-date="disabledChangeDate"/><el-select v-model="changeForm.slot" placeholder="选择时段"><el-option v-for="slot in slots" :key="slot" :label="slot" :value="slot"/></el-select></div>
      </el-form-item>
      <el-form-item :label="changeType === 'reschedule' ? '改期说明' : '取消原因'"><el-input v-model="changeForm.reason" type="textarea" :rows="4" maxlength="500" show-word-limit placeholder="请填写说明"/></el-form-item>
    </el-form>
    <template #footer><el-button @click="changeVisible = false">暂不提交</el-button><el-button type="primary" :loading="changeBusy" @click="submitChange">提交申请</el-button></template>
  </el-dialog>
  <BookingAlbum v-if="['delivered','completed','settled'].includes(workflow.stage)" :booking-id="detail.id"/><Approval ref="approvalRef" :tableName="tableName" @shChange="init()"/><payForm ref="pay" @payChange="getDetail()"/>
 </section>
</template>
<script setup>
import axios from 'axios'
import BookingAlbum from '@/components/BookingAlbum.vue'

import moment from 'moment'
import {
  ref,
  getCurrentInstance,
  watch,
  onUnmounted,
  onMounted,
  nextTick,
  computed,
  inject
} from 'vue';
import {
  ElMessageBox
} from 'element-plus'
import {
  useRoute,
  useRouter
} from 'vue-router';
import {
  useStore
} from 'vuex';
const store = useStore()
const user = computed(()=>store.getters['user/session'])
const userAvatar = computed(()=>store.getters['user/avatar'])
const context = getCurrentInstance()?.appContext.config.globalProperties;
const route = useRoute()
const router = useRouter()
//基础信息
const tableName = 'sheyingshiyuyue'
const formName = '摄影师预约'
//基础信息
const breadList = ref([{
  name: formName
}])
//权限验证
const btnAuth = (e,a)=>{
  if(centerType.value){
    return context?.$toolUtil.isBackAuth(e,a)
  }else{
    return context?.$toolUtil.isAuth(e,a)
  }
}
//查看权限验证
const btnFrontAuth = (e,a)=>{
  if(centerType.value){
    return context?.$toolUtil.isBackAuth(e,a)
  }else{
    return context?.$toolUtil.isFrontAuth(e,a)
  }
}
// 返回
const backClick = () =>{
  history.back()
}
// 轮播图
const bannerList = ref([])
// 详情
const title = ref('')
const detail = ref({}),workflow=ref({}),changeVisible=ref(false),changeBusy=ref(false),changeType=ref('reschedule'),changeForm=ref({date:'',slot:'',reason:''}),slots=['08:00-09:00','09:00-10:00','10:00-11:00','11:00-12:00','13:00-14:00','14:00-15:00','15:00-16:00','16:00-17:00'],stageLabels={confirmed:'待付款 / 待拍摄',shot:'拍摄完成，等待交付',delivered:'照片已交付',completed:'用户已确认完成',settled:'内部结算已登记'}
const activeName = ref('false')
const getDetail = () => {
  context?.$http({
    url: `${tableName}/detail/${route.query.id}`,
    method: 'get'
  }).then(res => {
    title.value = res.data.data.sheyingzhanghao
    detail.value = res.data.data
    context.$http.get('sheyingshiyuyue/workflow/'+route.query.id).then(r=>workflow.value=r.data.data)
  })
}
const downClick = (file) => {
  if(!file){
    context?.$toolUtil.message('文件不存在','error')
  }
  let arr = file.replace(new RegExp('file/', "g"), "")
  axios.get((location.href.split(context?.$config.name).length>1 ? location.href.split(context?.$config.name)[0] : '') + context?.$config.name + '/file/download?fileName=' + arr, {
    headers: {
      token: context?.$toolUtil.storageGet('frontToken')
    },
    responseType: "blob"
  }).then(({
             data
           }) => {
    const binaryData = [];
    binaryData.push(data);
    const objectUrl = window.URL.createObjectURL(new Blob(binaryData, {
      type: 'application/pdf;chartset=UTF-8'
    }))
    const a = document.createElement('a')
    a.href = objectUrl
    a.download = arr
    // a.click()
    // 下面这个写法兼容火狐
    a.dispatchEvent(new MouseEvent('click', {
      bubbles: true,
      cancelable: true,
      view: window
    }))
    window.URL.revokeObjectURL(data)
  })
}
// 判断是否从个人中心跳转
const centerType = ref(false)
const init = () => {
  if(route.query.centerType){
    centerType.value = true
  }
  getDetail()
}
//普通支付
import payForm from '@/components/payForm'
const pay = ref(null)
const payClick = ()=>{
  if(detail.value.sfsh!='是'){
    context?.$toolUtil.message('请审核通过后再支付', 'error')
    return false
  }
  pay.value.payClick(tableName,detail.value)
}
function requestChange(type){changeType.value=type;changeForm.value={date:'',slot:'',reason:''};changeVisible.value=true}
function disabledChangeDate(date){const today=new Date();today.setHours(0,0,0,0);return date.getTime()<today.getTime()}
async function submitChange(){
 if(changeBusy.value)return
 const type=changeType.value
 const {date,slot,reason}=changeForm.value
 if(type==='reschedule'){
  if(!date||!slot){context.$toolUtil.message('请选择日期和时间段','warning');return}
  const start=new Date(date+'T'+slot.slice(0,5)+':00+08:00')
  if(!Number.isFinite(start.getTime())||start.getTime()<=Date.now()){context.$toolUtil.message('请选择尚未开始的拍摄日期和时段','warning');return}
 }
 if(!reason?.trim()){context.$toolUtil.message('请填写说明','warning');return}
 changeBusy.value=true
 try{
  await context.$http.post('sheyingshiyuyue/change-request',{bookingId:detail.value.id,type,date:date||'',slot:slot||'',reason:reason.trim()})
  changeVisible.value=false
  context.$toolUtil.message('申请已提交，等待工作室处理','success')
 }catch(e){
  const message=e?.response?.data?.msg||e?.data?.msg||e?.message||'申请未提交，请稍后重试'
  context.$toolUtil.message(message,'error')
 }finally{changeBusy.value=false}
}
//审核
import Approval from '@/components/approval.vue'
const approvalRef = ref(null)
const approvalClick = (btnType='审核') => {
  if(!context?.$toolUtil.storageGet('frontToken')){
    context?.$toolUtil.message('请登录后再操作！','error')
    return false
  }
  if(!btnAuth('sheyingshiyuyue',btnType)){
    context?.$toolUtil.message('暂无权限操作！','error')
    return false
  }
  let row = detail.value
  let params = {
    id: row.id,
    sfsh: row.sfsh,
    shhf: row.shhf,
  }
  nextTick(() => {
    approvalRef.value.approvalClick(params)
  })
}
//修改
const editClick = () => {
  router.push(`/index/${tableName}Add?id=${detail.value.id}&&type=edit`)
}
//删除
const delClick = () => {
  ElMessageBox.confirm(`是否删除此${formName}？`, '提示', {
    confirmButtonText: '是',
    cancelButtonText: '否',
    type: 'warning',
  }).then(()=>{
    context?.$http({
      url: `${tableName}/delete`,
      method: 'post',
      data: [detail.value.id]
    }).then(res=>{
      context?.$toolUtil.message('删除成功','success',()=>{
        history.back()
      })
    })

  }).catch(_ => {})
}
onMounted(()=>{
  init()
})
</script>
<style scoped>
.booking-order{color:#304439}.order-heading{display:flex;justify-content:space-between;align-items:center;gap:16px;margin-bottom:26px}.order-heading span{font-size:10px;letter-spacing:2px;color:#94a08e}.order-heading h2{font-size:24px;font-weight:500;margin:10px 0}.order-heading p{font-size:12px;color:#8a9583;margin:0}.order-status{padding:24px 0;border-bottom:1px solid #edf0e8;margin-bottom:24px}.order-status p{font-size:13px;line-height:1.8;color:#7d8974;margin-bottom:0}.order-columns{display:grid;grid-template-columns:1fr 310px;gap:30px}.order-columns h3{font-size:16px;margin:0 0 22px;font-weight:600}dl{display:grid;grid-template-columns:90px minmax(0,1fr);gap:22px 12px;font-size:14px}dt{color:#8b9583}dd{margin:0;overflow-wrap:anywhere}.order-summary{padding:24px;background:#f6f8f3;border:1px solid #e6ebdf;border-radius:10px}.order-summary>span{font-size:12px;color:#8b9583}.order-summary strong{display:block;font-size:30px;font-weight:500;margin:14px 0 20px}.order-summary p{font-size:12px;line-height:1.9;color:#85917a}.order-summary>.el-button{width:100%;margin:20px 0 8px}.order-summary>.el-tag{margin-top:20px}.change-fields{display:flex;gap:10px;width:100%}.change-fields .el-date-editor,.change-fields .el-select{flex:1;min-width:0}.order-change-actions{display:flex;gap:10px;margin-top:14px}.order-change-actions .el-button{margin:0!important;width:auto!important}.booking-order .el-steps{font-size:12px}@media(max-width:1050px){.order-columns{grid-template-columns:1fr}.order-heading{align-items:flex-start}.order-heading h2{font-size:20px}}@media(max-width:560px){.booking-order :deep(.el-step__title){font-size:11px}.booking-order :deep(.el-steps--simple){padding:12px 8px}.order-summary{padding:18px}}
</style>
