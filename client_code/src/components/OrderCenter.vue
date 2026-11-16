<template>
<section class="order-center" v-loading="loading">
 <header><div><h2>我的订单</h2><p>查看拍摄安排、付款及照片交付进度</p></div><el-button @click="load">刷新</el-button></header>
 <nav aria-label="订单状态"><button v-for="tab in tabs" :key="tab.key" :class="{active:status===tab.key}" @click="changeTab(tab.key)">{{tab.label}}</button></nav>
 <el-alert v-if="error" :title="error" type="error" :closable="false"/>
 <el-empty v-if="!loading&&!error&&!orders.length" description="该分类暂无订单"/>
 <article v-for="order in orders" :key="order.id">
  <div class="order-top"><span>订单 #{{order.id}}</span><el-tag>{{order.refund_status ? refundLabels[order.refund_status] : labels[order.order_status]}}</el-tag></div>
  <div class="order-body"><div><h3>{{order.sheyingxingming || '摄影服务'}}</h3><p>{{order.yuyuesheyingriqi}} · {{order.yuyueshijianduan}}</p><p>{{order.package_name || '套餐待工作室确认'}}</p><small>{{order.service_content || '工作室确认档期、套餐和服务内容后开放付款'}}</small></div><div class="order-price"><strong>¥ {{Number(order.yuyuejiage||0).toFixed(2)}}</strong><small>{{order.ispay==='已支付'?'已付款 · 工作室统一收款':'未付款'}}</small></div></div>
  <footer><OrderRefund v-if="order.refund_status || (order.order_status==='shoot' && order.ispay==='已支付')" :booking-id="order.id" :amount="order.yuyuejiage" :status="order.refund_status" @changed="load"/><el-button @click="detail(order)">订单详情 / 变更申请</el-button><el-button v-if="order.order_status==='payment'" type="primary" @click="pay.payClick('sheyingshiyuyue',order)">核对并付款</el-button><BookingAlbum v-if="['delivered','completed'].includes(order.order_status)" :booking-id="order.id"/><el-button v-if="order.delivery_url" @click="delivery(order)">查看照片</el-button><el-button v-if="order.order_status==='delivered'" type="primary" :loading="completing===order.id" @click="complete(order)">确认服务完成</el-button><OrderReview v-if="order.order_status==='completed'&&!Number(order.reviewed)" :booking-id="order.id" @changed="load"/><el-tag v-if="Number(order.reviewed)" type="success">已评价</el-tag></footer>
 </article>
 <el-pagination v-if="total>10" background layout="total,prev,pager,next" :total="total" :page-size="10" v-model:current-page="page" @current-change="load"/>
 <payForm ref="pay" @payChange="load"/>
</section>
</template>
<script setup>
import {ref,getCurrentInstance,onMounted} from 'vue'
import {useRouter} from 'vue-router'
import payForm from '@/components/payForm.vue'
import BookingAlbum from '@/components/BookingAlbum.vue'
import OrderReview from '@/components/OrderReview.vue'
import OrderRefund from '@/components/OrderRefund.vue'
const ctx=getCurrentInstance().appContext.config.globalProperties,router=useRouter()
const tabs=[{key:'all',label:'全部'},{key:'pending',label:'待确认'},{key:'payment',label:'待付款'},{key:'shoot',label:'待拍摄'},{key:'delivery',label:'待交付'},{key:'delivered',label:'已交付'},{key:'completed',label:'已完成'},{key:'cancelled',label:'已取消 / 未通过'}]
const refundLabels={pending:'退款审核中',approved:'待登记退款',completed:'退款已完成',rejected:'退款申请已驳回'}
const labels=Object.fromEntries(tabs.map(t=>[t.key,t.label]))
const orders=ref([]),status=ref('all'),page=ref(1),total=ref(0),loading=ref(false),error=ref(''),pay=ref(),completing=ref(null)
let sequence=0
async function load(){const current=++sequence;loading.value=true;error.value='';try{const r=await ctx.$http.get('sheyingshiyuyue/orders',{params:{status:status.value,page:page.value}});if(current===sequence){orders.value=r.data.data;total.value=Number(r.data.total)}}catch(e){if(current===sequence)error.value=e?.message||'订单加载失败，请重试'}finally{if(current===sequence)loading.value=false}}
function changeTab(key){status.value=key;page.value=1;load()}
function detail(order){router.push({path:'/index/sheyingshiyuyueDetail',query:{id:order.id,centerType:'1'}})}
function delivery(order){if(/^https?:[/][/]/i.test(order.delivery_url))window.open(order.delivery_url,'_blank','noopener');else ctx.$toolUtil.message('交付链接不可用，请联系工作室','warning')}
async function complete(order){if(completing.value)return;completing.value=order.id;try{await ctx.$http.post('sheyingshiyuyue/orders/'+order.id+'/complete');ctx.$toolUtil.message('已确认服务完成','success');await load()}catch(e){ctx.$toolUtil.message(e?.message||'确认失败','error')}finally{completing.value=null}}
onMounted(load)
</script>
<style scoped>
.order-center header{display:flex;align-items:center;justify-content:space-between;gap:16px}.order-center h2{font-size:22px;color:#294a3b;margin:0 0 8px}.order-center header p{color:#849083;font-size:13px;margin:0}.order-center nav{display:flex;flex-wrap:wrap;gap:8px;margin:24px 0}.order-center nav button{padding:9px 13px;border:0;background:#f3f5f0;border-radius:6px;color:#6c7867;cursor:pointer}.order-center nav button.active{background:#305d4c;color:white}.order-center article{border:1px solid #e2e8dc;border-radius:10px;margin:16px 0;overflow:hidden}.order-top{display:flex;justify-content:space-between;align-items:center;padding:12px 18px;background:#f7f8f4;color:#819078;font-size:12px}.order-body{display:flex;justify-content:space-between;gap:24px;padding:20px}.order-body h3{margin:0 0 12px;color:#344d3c;font-size:17px}.order-body p{margin:8px 0;color:#697963;font-size:13px}.order-body small{font-size:12px;color:#8b9685;line-height:1.8;white-space:pre-wrap}.order-price{display:flex;flex-direction:column;gap:8px;text-align:right;flex-shrink:0}.order-price strong{font-size:23px;color:#305d4c;font-weight:500}.order-center footer{display:flex;justify-content:flex-end;flex-wrap:wrap;gap:8px;padding:12px 18px;border-top:1px solid #eef1e9}.order-center footer .el-button{margin:0}.order-center .el-pagination{justify-content:center;margin-top:24px}@media(max-width:600px){.order-body{flex-direction:column}.order-price{text-align:left}.order-center footer{justify-content:flex-start}}
</style>
