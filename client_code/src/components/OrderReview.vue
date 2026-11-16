<template>
 <el-button type="primary" @click="visible=true">评价摄影师</el-button>
 <el-dialog v-model="visible" title="分享你的拍摄体验" width="min(560px,94vw)" append-to-body :close-on-click-modal="!busy">
  <el-form label-position="top"><el-form-item label="整体评分"><el-rate v-model="score" show-text/></el-form-item><el-form-item label="拍摄体验"><el-input v-model="content" type="textarea" :rows="5" maxlength="2000" show-word-limit placeholder="说说服务、沟通和成片体验…"/></el-form-item><el-form-item label="评价图片（选填，最多6张；发布后公开展示）"><div class="review-images"><div v-for="(path,i) in images" :key="path"><img :src="ctx.$config.url+path"/><el-button text @click="images.splice(i,1)">移除</el-button></div><label v-if="images.length<6">{{uploading?'上传中…':'添加图片'}}<input type="file" accept="image/jpeg,image/png,image/webp" :disabled="uploading||busy" @change="upload"/></label></div></el-form-item></el-form>
  <el-alert v-if="error" :title="error" type="error" :closable="false"/>
  <template #footer><el-button :disabled="busy" @click="visible=false">稍后评价</el-button><el-button type="primary" :loading="busy" :disabled="uploading" @click="submit">发布评价</el-button></template>
 </el-dialog>
</template>
<script setup>
import {ref,getCurrentInstance} from 'vue'
import axios from 'axios'
const props=defineProps({bookingId:[String,Number]}),emit=defineEmits(['changed']),ctx=getCurrentInstance().appContext.config.globalProperties
const visible=ref(false),score=ref(5),content=ref(''),images=ref([]),busy=ref(false),uploading=ref(false),error=ref('')
async function upload(event){const file=event.target.files[0];if(!file)return;uploading.value=true;error.value='';try{if(!['image/jpeg','image/png','image/webp'].includes(file.type)||file.size>5*1024*1024)throw new Error('请选择5MB以内的JPEG、PNG或WebP图片');const form=new FormData();form.append('file',file);const r=await axios.post(process.env.VUE_APP_BASE_API+'/file/upload',form,{headers:{Token:localStorage.getItem('frontToken')}});if(r.data.code!==0)throw new Error(r.data.msg||'上传失败');images.value.push('file/'+r.data.file)}catch(e){error.value=e.message||'上传失败'}finally{uploading.value=false;event.target.value=''}}
async function submit(){if(busy.value)return;if(!score.value||!content.value.trim()){error.value='请选择评分并填写评价';return}busy.value=true;error.value='';try{await ctx.$http.post('sheyingshiyuyue/orders/'+props.bookingId+'/review',{score:score.value,content:content.value,images:images.value.join(',')});visible.value=false;ctx.$toolUtil.message('评价已发布','success');emit('changed')}catch(e){error.value=e?.data?.msg||e?.message||'评价发布失败'}finally{busy.value=false}}
</script>
<style scoped>.review-images{display:flex;gap:12px;flex-wrap:wrap}.review-images>div{display:flex;flex-direction:column}.review-images img{width:80px;height:80px;object-fit:cover;border-radius:6px}.review-images label{display:flex;align-items:center;justify-content:center;border:1px dashed #a5b49d;border-radius:6px;width:90px;height:80px;cursor:pointer;color:#6a8061}.review-images input{display:none}</style>
