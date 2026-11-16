<template>
  <OrderCenter v-if="route.query.centerType && context.$toolUtil.storageGet('frontSessionTable')==='yonghu'"/>
  <div v-else class="list-page" :style='{}'>
    <div class="breadcrumb-wrapper" style="width: 100%;">
      <div class="bread_view">
        <el-breadcrumb separator="" class="breadcrumb">
          <el-breadcrumb-item class="first_breadcrumb" :to="{ path: '/' }">首页</el-breadcrumb-item>
          <el-breadcrumb-item class="second_breadcrumb" v-for="(item,index) in breadList" :key="index">{{item.name}}</el-breadcrumb-item>
        </el-breadcrumb>
      </div>
      <div class="back_view" v-if="centerType">
        <el-button class="back_btn" @click="backClick" type="primary">返回</el-button>
      </div>
    </div>
    <el-form v-if="!centerType" :inline="true" :model="searchQuery" class="list_search" @submit.native.prevent>

      <div class="search_view">
        <div class="search_label">
          摄影账号：
        </div>
        <div class="search_box">
          <el-select
              class="search_sel"
              clearable
              v-model="searchQuery.sheyingzhanghao"
              placeholder="摄影账号"
          >
            <el-option v-for="item in sheyingzhanghaoLists" :label="item" :value="item"></el-option>
          </el-select>
        </div>
      </div>
      <div class="search_btn_view">
        <el-button class="search_btn" type="primary" @click="searchClick">搜索</el-button>
        <el-button class="add_btn" type="success" v-if="btnAuth('sheyingshiyuyue','新增')" @click="addClick">新增</el-button>
        <el-button v-if="context.$toolUtil.storageGet('frontRole') === '管理员'" class="del_btn" type="danger" @click="delClick(null)">
          删除
        </el-button>
      </div>
    </el-form>
    <section v-if="centerType" class="past-photographers"><header><h3>预约过的摄影师</h3><el-button text @click="loadHistory">刷新</el-button></header><div v-if="photographers.length" class="photographer-history"><article v-for="item in photographers" :key="item.sheyingzhanghao"><strong>{{item.sheyingxingming}}</strong><span>预约 {{item.bookings}} 次 · 最近 {{String(item.latestDate).slice(0,10)}}</span><el-button v-if="item.profileId" size="small" @click="router.push('/index/sheyingshixinxiDetail?id='+item.profileId)">查看摄影师 / 再次预约</el-button><small v-else>该摄影师暂未开放预约</small></article></div><el-empty v-else description="暂无历史预约摄影师" :image-size="50"/></section>
    <div class="page_list">
      <div class="data_box">
        <div class="table_view">
          <el-table v-loading="listLoading" @selection-change="handleSelectionChange" class="data_table" :data="list" border :row-style="{'cursor':'pointer'}"
                    @row-click="tableDetailClick" :stripe='true'>
            <el-table-column :resizable='true' align="left" header-align="left" type="selection" width="55" />
            <el-table-column min-width="120" label="序号" width="120" :resizable='true' align="left" header-align="left">
              <template #default="scope">{{ (listQuery.page-1)*listQuery.limit+scope.$index + 1}}</template>
            </el-table-column>
            <el-table-column min-width="120" label="摄影账号" :resizable='true' align="left" header-align="left">
              <template #default="scope">
                {{scope.row.sheyingzhanghao}}
              </template>
            </el-table-column>
            <el-table-column min-width="120" label="摄影姓名" :resizable='true' align="left" header-align="left">
              <template #default="scope">
                {{scope.row.sheyingxingming}}
              </template>
            </el-table-column>
            <el-table-column min-width="120" label="预约价格" :resizable='true' align="left" header-align="left">
              <template #default="scope">
                {{scope.row.yuyuejiage}}
              </template>
            </el-table-column>
            <el-table-column min-width="120" label="用户账号" :resizable='true' align="left" header-align="left">
              <template #default="scope">
                {{scope.row.yonghuzhanghao}}
              </template>
            </el-table-column>
            <el-table-column min-width="120" label="用户姓名" :resizable='true' align="left" header-align="left">
              <template #default="scope">
                {{scope.row.yonghuxingming}}
              </template>
            </el-table-column>
            <el-table-column min-width="120" label="预约摄影日期" :resizable='true' align="left" header-align="left">
              <template #default="scope">
                {{scope.row.yuyuesheyingriqi}}
              </template>
            </el-table-column>
            <el-table-column min-width="120" label="预约摄影时间" :resizable='true' align="left" header-align="left">
              <template #default="scope">
                {{scope.row.yuyueshijianduan}}
              </template>
            </el-table-column>            <el-table-column min-width="110" label="支付状态" :resizable='true' align="left" header-align="left">
              <template #default="scope"><el-tag class="payment-status" :type="scope.row.ispay=='已支付'?'success':'info'" effect="light">{{scope.row.ispay=='已支付'?'已支付':'待支付'}}</el-tag></template>
            </el-table-column>
            <el-table-column min-width="130" label="支付操作" :resizable='true' align="left" header-align="left">
              <template #default="scope">
                <el-button v-if="scope.row.ispay!='已支付' && scope.row.sfsh=='是' && btnAuth('sheyingshiyuyue','支付')" class="payment-action" type="primary" size="small" @click.stop="payClick(scope.row)">确认付款</el-button>
                <span v-else-if="scope.row.ispay=='已支付'" class="payment-complete">已完成</span><span v-else class="payment-waiting">待审核</span>
              </template>
            </el-table-column>
            <el-table-column min-width="120" label="审核回复" v-if="centerType" :resizable='true' align="left" header-align="left">
              <template #default="scope">
                {{scope.row.shhf}}
              </template>
            </el-table-column>
            <el-table-column min-width="120" label="审核状态" v-if="centerType" :resizable='true' align="left" header-align="left">
              <template #default="scope">
                <el-tag type="success" v-if="scope.row.sfsh=='是'">通过</el-tag>
                <el-tag type="danger" v-if="scope.row.sfsh=='否'">未通过</el-tag>
                <el-tag type="warning" v-if="scope.row.sfsh=='待审核'">待审核</el-tag>
              </template>
            </el-table-column>
          </el-table>
        </div>
        <el-pagination
            background
            :layout="layouts.join(',')"
            :total="total"
            :page-size="listQuery.limit"
            v-model:current-page="listQuery.page"
            prev-text="上一页"
            next-text="下一页"
            :hide-on-single-page="false"
            :style='{}'
            @size-change="sizeChange"
            @current-change="currentChange"/>
      </div>
    </div>
    <payForm ref="pay" @payChange="searchClick()"></payForm>
    <el-dialog v-model="preViewVisible" :title="'查看大图'" width="40%" destroy-on-close>
      <div style="text-align:center">
        <img :src="preViewUrl" style="max-width: 100%;" alt="">
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import OrderCenter from '@/components/OrderCenter.vue'
import {
  ref,
  getCurrentInstance,
  nextTick,
  computed,
  inject,
} from 'vue';
import {
  useRoute,
  useRouter
} from 'vue-router';
import {
  useStore
} from 'vuex';
import {
  ElMessageBox
} from 'element-plus'
const store = useStore()
const user = computed(()=>store.getters['user/session'])
const context = getCurrentInstance()?.appContext.config.globalProperties;
const router = useRouter()
const route = useRoute()
//基础信息
const tableName = 'sheyingshiyuyue'
const formName = '摄影师预约'
//基础信息
const breadList = ref([{
  name: formName
}])
const list = ref([])
const selRows = ref([])
const listQuery = ref({
  page: 1,
  limit: 20,
})
const total = ref(0)
const listLoading = ref(false)
//权限验证
const btnAuth = (e,a)=>{
  if(centerType.value){
    return context?.$toolUtil.isBackAuth(e,a)
  }else{
    return context?.$toolUtil.isAuth(e,a)
  }
}
const addClick = () => {
  router.push('/index/sheyingshiyuyueAdd')
}
//判断是否从个人中心跳转
const centerType = ref(false)
//返回
const backClick = () => {
  router.push(`/index/${context?.$toolUtil.storageGet('frontSessionTable')}Center`)
}
//删
const delClick = (id) => {
  let ids = []
  if (id) {
    ids = [id]
  } else {
    if (selRows.value.length) {
      for (let x in selRows.value) {
        ids.push(selRows.value[x].id)
      }
    } else {
      return false
    }
  }
  ElMessageBox.confirm(`是否删除选中${formName}`, '提示', {
    confirmButtonText: '是',
    cancelButtonText: '否',
    type: 'warning',
  }).then(() => {
    context.$http({
      url: `${tableName}/delete`,
      method: 'post',
      data: ids
    }).then(res => {
      context?.$toolUtil.message('删除成功', 'success',()=>{
        getList()
      })
    })
  }).catch(_ => {})
}
//多选
const handleSelectionChange = (e) => {
  selRows.value = e
  console.log(selRows.value); // 添加调试日志
}
//搜索
const searchQuery = ref({})
//下拉列表
const sheyingzhanghaoLists = ref([])
const getsheyingzhanghaoLists = () => {
  context?.$http({
    url: 'option/sheyingshi/sheyingzhanghao',
    method:'get'
  }).then(res=>{
    sheyingzhanghaoLists.value = res.data.data
  })
}
getsheyingzhanghaoLists()
const searchClick = () => {
  listQuery.value.page = 1
  getList()
}
//分页
const layouts = ref(["total","prev","pager","next"])
const sizeChange = (size) => {
  listQuery.value.limit = size
  getList()
}
const currentChange = (page) => {
  listQuery.value.page = page
  getList()
}
//分页
//列表
const getList = () => {
  listLoading.value = true
  let params = JSON.parse(JSON.stringify(listQuery.value))
  if(searchQuery.value.sheyingzhanghao&&searchQuery.value.sheyingzhanghao!=''){
    params.sheyingzhanghao = searchQuery.value.sheyingzhanghao
  }
  if(!centerType.value){
    params['sfsh'] = '是';
  }
  context?.$http({
    url: `${tableName}/${centerType.value?'page':'list'}`,
    method: 'get',
    params: params
  }).then(res => {
    listLoading.value = false
    list.value = res.data.data.list
    total.value = Number(res.data.data.total)
  })
}
const tableDetailClick = (row) => {
  router.push(`${tableName}Detail?id=` + row.id + (centerType.value?'&&centerType=1':''))
}
//下载文件
const download = (file) =>{
  if(!file){
    context?.$toolUtil.message('文件不存在','error')
  }
  const a = document.createElement('a');
  a.style.display = 'none';
  a.setAttribute('target', '_blank');
  file && a.setAttribute('download', file);
  a.href = context?.$config.url + file;
  document.body.appendChild(a);
  a.click();
  document.body.removeChild(a);
}
// 查看大图
const preViewUrl = ref('')
const preViewVisible = ref(false)
const preViewClick = (url) =>{
  preViewUrl.value = url
  preViewVisible.value = true
}
//普通支付
import payForm from '@/components/payForm'
const pay = ref(null)
const payClick = (row)=>{
  pay.value.payClick(tableName,row)
}
const photographers=ref([])
async function loadHistory(){try{const res=await context.$http.get('sheyingshiyuyue/photographerHistory');photographers.value=res.data.data}catch{context.$toolUtil.message('历史摄影师加载失败，请重试','error')}}
const init = async() => {
  if(route.query.centerType){
    centerType.value = true;loadHistory()
  }
  if(context.$toolUtil.storageGet('frontToken') && !user.value.id){
    await store.dispatch("user/getSession")
  }
  getList()
}
init()
</script>

<style scoped>.past-photographers{margin-bottom:26px}.past-photographers header{display:flex;justify-content:space-between;align-items:center}.past-photographers h3{font-size:16px}.photographer-history{display:flex;gap:12px;overflow:auto;padding-bottom:10px}.photographer-history article{min-width:230px;padding:18px;border:1px solid #e5eadf;border-radius:8px;display:flex;align-items:flex-start;flex-direction:column;gap:12px}.photographer-history span{font-size:12px;color:#8b9682}</style><style scoped>
.payment-status{min-width:62px;justify-content:center;border-radius:5px;font-size:12px}.payment-action{border-radius:6px;padding:7px 13px;font-size:12px}.payment-complete{color:#4a8f72;font-size:12px}.payment-waiting{color:#a3ada4;font-size:12px}
</style>