<template>
 <el-button plain @click="open">{{status?'退款进度':'申请退款'}}</el-button>
 <el-dialog v-model="visible" title="订单退款" width="min(520px,94vw)" append-to-body :close-on-click-modal="!busy">
  <el-skeleton v-if="loading" :rows="3" animated/>
  <template v-else-if="record"><el-tag :type="record.status==='rejected'?'danger':'warning'">{{labels[record.status]}}</el-tag><dl><dt>申请金额</dt><dd>¥ {{Number(record.amount).toFixed(2)}}</dd><dt>退款原因</dt><dd>{{record.reason}}</dd><dt>申请时间</dt><dd>{{record.created_at}}</dd><dt>审核说明</dt><dd>{{record.admin_note||'等待工作室审核'}}</dd><dt>退款结果</dt><dd>{{record.result_note||'尚未登记'}}</dd><dt v-if="record.completed_at">完成时间</dt><dd v-if="record.completed_at">{{record.completed_at}}</dd></dl></template>
  <template v-else-if="!error"><el-alert title="提交后由工作室审核。批准后预约取消并释放档期，原付款记录保留。" type="info" :closable="false"/><p class="refund-amount">本次申请退款 ¥ {{Number(amount||0).toFixed(2)}}</p><el-input v-model="reason" type="textarea" :rows="4" maxlength="500" show-word-limit placeholder="请说明退款原因"/></template>
  <el-alert v-if="error" :title="error" type="error" :closable="false" style="margin-top:14px"/>
  <template #footer><el-button :disabled="busy" @click="visible=false">关闭</el-button><el-button v-if="!record&&!loading" type="primary" :loading="busy" @click="apply">提交退款申请</el-button></template>
 </el-dialog>
</template>
<script setup>
import {ref,getCurrentInstance} from 'vue'
const props=defineProps({bookingId:[String,Number],amount:[Number,String],status:String}),emit=defineEmits(['changed']),ctx=getCurrentInstance().appContext.config.globalProperties
const visible=ref(false),loading=ref(false),busy=ref(false),record=ref(null),reason=ref(''),error=ref('')
const labels={pending:'等待审核',approved:'已批准 · 待登记退款',rejected:'申请已驳回',completed:'退款已完成'}
async function open(){visible.value=true;loading.value=true;error.value='';try{const r=await ctx.$http.get('booking-refunds/booking/'+props.bookingId);record.value=r.data.data}catch(e){error.value=e?.data?.msg||e?.message||'退款记录加载失败'}finally{loading.value=false}}
async function apply(){if(busy.value)return;if(!reason.value.trim()){error.value='请填写退款原因';return}busy.value=true;error.value='';try{await ctx.$http.post('booking-refunds/booking/'+props.bookingId,{reason:reason.value.trim()});ctx.$toolUtil.message('退款申请已提交','success');emit('changed');await open()}catch(e){error.value=e?.data?.msg||e?.message||'退款申请失败'}finally{busy.value=false}}
</script>
<style scoped>dl{display:grid;grid-template-columns:90px 1fr;gap:16px;font-size:13px;margin:22px 0}dt{color:#8c9786}dd{margin:0;white-space:pre-wrap;overflow-wrap:anywhere;color:#42563d}.refund-amount{font-size:20px;color:#305d4c;margin:24px 0}</style>
