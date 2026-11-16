<template>
 <el-badge :value="unread" :hidden="!unread" :max="99" class="inbox-badge"><el-button text aria-label="查看站内消息" @click="open">消息</el-button></el-badge>
</template>
<script setup>
import {ref,getCurrentInstance,onMounted,onBeforeUnmount,watch} from 'vue'
import {useRouter,useRoute} from 'vue-router'
const props=defineProps({tokenKey:{type:String,default:'frontToken'},destination:{type:String,default:'/index/messageCenter'}})
const ctx=getCurrentInstance().appContext.config.globalProperties,router=useRouter(),route=useRoute(),unread=ref(0)
let timer,busy=false,disposed=false
async function refresh(){
 if(disposed||busy)return
 if(!localStorage.getItem(props.tokenKey)){unread.value=0;return}
 busy=true
 try{const r=await ctx.$http.get('sheyingshiyuyue/messages/unread');if(!disposed)unread.value=Number(r.data.data)||0}catch{}finally{busy=false}
}
function open(){router.push(props.destination)}
onMounted(()=>{refresh();timer=setInterval(refresh,30000);window.addEventListener('huahai-messages-changed',refresh)})
watch(()=>route.fullPath,refresh)
onBeforeUnmount(()=>{disposed=true;clearInterval(timer);window.removeEventListener('huahai-messages-changed',refresh)})
</script>
<style scoped>.inbox-badge{display:inline-flex;margin-right:14px;vertical-align:middle}.inbox-badge :deep(.el-button){font-size:13px}</style>
