<template>
 <el-button :link="staff" @click.stop="open">{{staff?'订单相册':'查看照片'}}</el-button>
 <el-dialog v-model="visible" title="订单照片相册" width="min(900px,94vw)" append-to-body @closed="clearPhotos">
  <el-alert v-if="error" :title="error" type="error" :closable="false"/>
  <p class="album-note">{{staff?'原片、精修片和拍摄花絮按订单整理。管理员发布交付后，客户才能查看照片。':'照片由工作室交付，仅当前订单客户可查看。'}}</p>
  <div class="album-tools"><el-select v-model="category" style="width:150px"><el-option v-for="(label,key) in categories" :key="key" :label="label" :value="key"/></el-select><el-button :loading="loading" @click="load">刷新</el-button><template v-if="staff&&stage==='shot'"><label class="album-upload">{{uploading?'上传中…':'上传照片'}}<input type="file" multiple accept="image/jpeg,image/png" :disabled="uploading" @change="upload"/></label><el-button v-if="admin" type="primary" :disabled="!photos.length||uploading" :loading="publishing" @click="publish">发布交付并通知客户</el-button></template></div>
  <div v-loading="loading" class="album-grid"><figure v-for="photo in filtered" :key="photo.id"><el-image v-if="photo.url" :src="photo.url" :preview-src-list="preview" :initial-index="preview.indexOf(photo.url)" fit="cover" preview-teleported/><div v-else class="album-placeholder">照片加载失败</div><figcaption><span>{{photo.original_name}}</span><small>{{categories[photo.category]}}</small><el-button size="small" @click="download(photo)">下载</el-button></figcaption></figure></div>
  <el-empty v-if="!loading&&!filtered.length" description="暂无此类照片"/>
  <template #footer><el-button @click="visible=false">关闭</el-button></template>
 </el-dialog>
</template>
<script setup>
import {ref,computed,getCurrentInstance,onBeforeUnmount} from 'vue'
import axios from 'axios'
const props=defineProps({bookingId:[Number,String],staff:Boolean}),emit=defineEmits(['changed'])
const ctx=getCurrentInstance().appContext.config.globalProperties,visible=ref(false),photos=ref([]),loading=ref(false),uploading=ref(false),publishing=ref(false),error=ref(''),category=ref('retouched'),stage=ref('')
const admin=props.staff&&localStorage.getItem('sessionTable')==='users',categories={original:'原片',retouched:'精修片',behind:'拍摄花絮'}
const filtered=computed(()=>photos.value.filter(p=>p.category===category.value)),preview=computed(()=>filtered.value.map(p=>p.url).filter(Boolean))
const raw=axios.create({baseURL:process.env.VUE_APP_BASE_API,withCredentials:true,timeout:60000})
function headers(){return {Token:localStorage.getItem(props.staff?'Token':'frontToken')||''}}
let sequence=0
function clearPhotos(){++sequence;photos.value.forEach(p=>{if(p.url)URL.revokeObjectURL(p.url)});photos.value=[];loading.value=false}
async function open(){visible.value=true;await load()}
async function load(){clearPhotos();const request=sequence;loading.value=true;error.value='';try{
 if(props.staff){const flow=await ctx.$http.get('sheyingshiyuyue/workflow/'+props.bookingId);stage.value=flow.data.data.stage}
 const r=await ctx.$http.get('booking-photos/'+props.bookingId)
 const results=await Promise.all((r.data.data||[]).map(async p=>{try{const file=await raw.get('booking-photos/file/'+p.id,{headers:headers(),responseType:'blob'});if(!file.data.type.startsWith('image/'))throw new Error('照片访问失败');return {...p,url:URL.createObjectURL(file.data)}}catch{return {...p,url:null}}}))
 if(request!==sequence){results.forEach(p=>p.url&&URL.revokeObjectURL(p.url));return}photos.value=results
 }catch(e){if(request===sequence)error.value=e?.data?.msg||e?.message||'相册加载失败'}finally{if(request===sequence)loading.value=false}}
async function upload(event){if(uploading.value)return;uploading.value=true;error.value='';try{for(const file of event.target.files){if(file.size>20*1024*1024)throw new Error('每张照片不能超过20MB');const form=new FormData();form.append('file',file);form.append('category',category.value);const r=await raw.post('booking-photos/'+props.bookingId,form,{headers:headers()});if(r.data.code!==0)throw new Error(r.data.msg||'上传失败')}await load()}catch(e){error.value=e?.response?.data?.message||e?.message||'上传失败'}finally{uploading.value=false;event.target.value=''}}
async function publish(){if(publishing.value)return;publishing.value=true;try{await ctx.$http.post('booking-photos/'+props.bookingId+'/publish');emit('changed');ctx.$toolUtil.message('已交付并通知客户','success');await load()}catch(e){error.value=e?.data?.msg||e?.message||'交付失败'}finally{publishing.value=false}}
function download(photo){if(!photo.url){error.value='照片加载失败，请刷新重试';return}const a=document.createElement('a');a.href=photo.url;a.download=photo.original_name;document.body.appendChild(a);a.click();a.remove()}
onBeforeUnmount(clearPhotos)
</script>
<style scoped>.album-note{color:#7a8775;line-height:1.8;font-size:13px}.album-tools{display:flex;gap:10px;flex-wrap:wrap;align-items:center;margin:20px 0}.album-upload{border:1px solid #305d4c;border-radius:5px;padding:8px 14px;cursor:pointer;color:#305d4c}.album-upload input{display:none}.album-grid{display:grid;grid-template-columns:repeat(3,minmax(0,1fr));gap:16px}.album-grid figure{margin:0;border:1px solid #e3e8df;border-radius:8px;overflow:hidden}.album-grid .el-image,.album-placeholder{width:100%;height:180px;background:#f3f5ef}.album-grid figcaption{padding:12px;display:flex;gap:8px;align-items:center;flex-wrap:wrap}.album-grid figcaption span{width:100%;overflow:hidden;text-overflow:ellipsis;white-space:nowrap;color:#42553f;font-size:12px}.album-grid small{color:#91a087;margin-right:auto}@media(max-width:650px){.album-grid{grid-template-columns:repeat(2,minmax(0,1fr))}.album-grid .el-image{height:140px}}</style>
