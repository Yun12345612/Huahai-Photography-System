<template>
  <div>
    <div class="center_view">
      <div class="list_search_view">
        <el-form :model="searchQuery" class="search_form" >
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
          <div class="search_view">
            <div class="search_label">
              支付状态：
            </div>
            <div class="search_box">
              <el-select
                  class="search_sel"
                  clearable
                  v-model="searchQuery.ispay"
                  placeholder="支付状态"
              >
                <el-option label="已支付" value="已支付"></el-option>
                <el-option label="未支付" value="未支付"></el-option>
              </el-select>
            </div>
          </div>
          <div class="search_view">
            <div class="search_label">
              审核状态：
            </div>
            <div class="search_box">
              <el-select
                  class="search_sel"
                  clearable
                  v-model="searchQuery.sfsh"
                  placeholder="审核状态"
              >
                <el-option v-for="item in approvalLists" :label="item" :value="item"></el-option>
              </el-select>
            </div>
          </div>
          <div class="search_btn_view">
            <el-button class="search_btn" type="primary" @click="searchClick()" size="default">搜索</el-button>
          </div>
        </el-form>
        <div class="btn_view"><el-button plain @click="$router.push('/booking-refunds')">退款管理</el-button>
          <el-button class="calendar_btn" type="warning" plain @click="$router.push('/booking-changes')">变更申请</el-button>          <el-button class="calendar_btn" type="primary" plain @click="$router.push('/booking-calendar')">预约日历</el-button>
          <el-button class="add_btn" type="primary" @click="addClick" v-if="btnAuth('sheyingshiyuyue','新增')">
            新增
          </el-button>
          <el-button class="del_btn" type="default" :disabled="selRows.length?false:true" @click="delClick(null)"  v-if="btnAuth('sheyingshiyuyue','删除')" link>
            删除
          </el-button>
        </div>
      </div>
      <el-table
          v-loading="listLoading"
          border
          :stripe='false'
          @selection-change="handleSelectionChange"
          ref="table"
          v-if="btnAuth('sheyingshiyuyue','查看')"
          :data="list"
          @row-click="listChange">
        <el-table-column :resizable='true' align="left" header-align="left" type="selection" width="55" />
        <el-table-column label="序号" width="70" :resizable='true' align="left" header-align="left">
          <template #default="scope">{{ (listQuery.page-1)*listQuery.limit+scope.$index + 1}}</template>
        </el-table-column>
        <el-table-column min-width="140"
                         :resizable='true'
                         :sortable='true'
                         align="left"
                         header-align="left"
                         prop="sheyingzhanghao"
                         label="摄影账号">
          <template #default="scope">
            {{scope.row.sheyingzhanghao}}
          </template>
        </el-table-column>
        <el-table-column min-width="140"
                         :resizable='true'
                         :sortable='true'
                         align="left"
                         header-align="left"
                         prop="sheyingxingming"
                         label="摄影姓名">
          <template #default="scope">
            {{scope.row.sheyingxingming}}
          </template>
        </el-table-column>
        <el-table-column min-width="140"
                         :resizable='true'
                         :sortable='true'
                         align="left"
                         header-align="left"
                         prop="yuyuejiage"
                         label="预约价格">
          <template #default="scope">
            {{scope.row.yuyuejiage}}
          </template>
        </el-table-column>
        <el-table-column min-width="140"
                         :resizable='true'
                         :sortable='true'
                         align="left"
                         header-align="left"
                         prop="yonghuzhanghao"
                         label="用户账号">
          <template #default="scope">
            {{scope.row.yonghuzhanghao}}
          </template>
        </el-table-column>
        <el-table-column min-width="140"
                         :resizable='true'
                         :sortable='true'
                         align="left"
                         header-align="left"
                         prop="yonghuxingming"
                         label="用户姓名">
          <template #default="scope">
            {{scope.row.yonghuxingming}}
          </template>
        </el-table-column>
        <el-table-column min-width="140"
                         :resizable='true'
                         :sortable='true'
                         align="left"
                         header-align="left"
                         prop="yonghuxingming"
                         label="联系电话">
          <template #default="scope">
            {{scope.row.lianxidianhua}}
          </template>
        </el-table-column>
        <el-table-column min-width="140"
                         :resizable='true'
                         :sortable='true'
                         align="left"
                         header-align="left"
                         prop="yuyuesheyingriqi"
                         label="预约摄影日期">
          <template #default="scope">
            {{scope.row.yuyuesheyingriqi}}
          </template>
        </el-table-column>
        <el-table-column min-width="140"
                         :resizable='true'
                         :sortable='true'
                         align="left"
                         header-align="left"
                         prop="yuyuesheyingriqi"
                         label="预约摄影时间">
          <template #default="scope">
            {{scope.row.yuyueshijianduan}}
          </template>
        </el-table-column>
        <el-table-column prop="ispay" label="支付状态" min-width="140" :resizable='true' :sortable='true' align="left" header-align="left">
          <template #default="scope">
            <span style="margin-right:10px">{{scope.row.ispay=='已支付'?'已支付':'未支付'}}</span>
            <el-button v-if="scope.row.ispay!='已支付' && btnAuth('sheyingshiyuyue','支付')" size="default" @click.stop="payClick(scope.row)">支付</el-button>
          </template>
        </el-table-column>
        <el-table-column label="审核回复" min-width="140" :resizable='true' :sortable='true' align="left" header-align="left">
          <template #default="scope">
            {{scope.row.shhf}}
          </template>
        </el-table-column>
        <el-table-column prop="sfsh" label="审核状态" min-width="140" :resizable='true' :sortable='true' align="left" header-align="left">
          <template #default="scope">
            <div v-if="scope.row.sfsh=='是'" style="text-align: center">
              <img src="@/assets/img/pass.png" style="width: 50px;"/>
              <div>通过</div>
            </div>
            <div v-else-if="scope.row.sfsh=='否'" style="text-align: center">
              <img src="@/assets/img/reject.png" style="width: 50px;"/>
              <div>未通过</div>
            </div>
            <div v-else style="text-align: center">
              <img src="@/assets/img/wait.png" style="width: 50px;"/>
              <div>待审核</div>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="审核" v-if="btnAuth('sheyingshiyuyue','审核')" :resizable='true' :sortable='true' align="left" header-align="left">
          <template #default="scope">
            <el-button v-if="scope.row.sfsh!='是'" size="default" @click="approvalClick(scope.row)">审核</el-button>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="300" :resizable='true' :sortable='true' align="left" header-align="left">
          <template #default="scope"><BookingWorkflow :row="scope.row" @changed="searchClick"/><BookingAlbum :booking-id="scope.row.id" staff @changed="searchClick"/>
            <el-button class="view_btn" type="default" v-if=" btnAuth('sheyingshiyuyue','查看')" @click="infoClick(scope.row.id)" link>
              详情
            </el-button>
            <el-button class="edit_btn" type="primary" @click="editClick(scope.row.id)" v-if=" btnAuth('sheyingshiyuyue','修改')" link>
              修改						</el-button>
            <el-button class="del_btn" type="default" @click="delClick(scope.row.id)"  v-if="btnAuth('sheyingshiyuyue','删除')" link>
              删除						</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
          background
          :layout="layouts.join(',')"
          :total="total"
          :page-size="listQuery.limit"
          v-model:current-page="listQuery.page"
          prev-text="<"
          next-text=">"
          :hide-on-single-page="false"
          :style='{"width":"100%","padding":"0","margin":"20px 0 0","whiteSpace":"nowrap","color":"#333","fontWeight":"500"}'
          :page-sizes="[10, 20, 30, 40, 50, 100]"
          @size-change="sizeChange"
          @current-change="currentChange"  />
    </div>
    <formModel ref="formRef" @formModelChange="formModelChange"></formModel>
    <payForm ref="pay" @payChange="searchClick()"></payForm>
    <Approval ref="approvalRef" :tableName="tableName" @shChange="shChange"></Approval>
  </div>
</template>
<script setup>
import BookingWorkflow from '@/components/BookingWorkflow.vue'
import BookingAlbum from '@/components/BookingAlbum.vue'
import axios from 'axios'
import moment from "moment"
import {
  reactive,
  ref,
  getCurrentInstance,
  nextTick,
  onMounted,
  watch,
  computed,
} from 'vue'
import {
  useRoute,
  useRouter
} from 'vue-router'
import {
  ElMessageBox
} from 'element-plus'
import {
  useStore
} from 'vuex';
const store = useStore()
const user = computed(()=>store.getters['user/session'])
const avatar = ref(store.state.user.avatar)
const context = getCurrentInstance()?.appContext.config.globalProperties;
import formModel from './formModel.vue'
//基础信息

const tableName = 'sheyingshiyuyue'
const formName = '摄影师预约'
const route = useRoute()
const router = useRouter()
//基础信息
onMounted(()=>{
})
//列表数据
const list = ref(null)
const table = ref(null)
const listQuery = ref({
  page: 1,
  limit: 20,
  sort: 'id',
  order: 'desc'
})
const searchQuery = ref({})
const selRows = ref([])
const listLoading = ref(false)
const listChange = (row) =>{
  nextTick(()=>{
    //table.value.clearSelection()
    table.value.toggleRowSelection(row)
  })
}
//列表
const getList = () => {
  listLoading.value = true
  let params = JSON.parse(JSON.stringify(listQuery.value))
  params['sort'] = 'id'
  params['order'] = 'desc'
  if(searchQuery.value.sheyingzhanghao&&searchQuery.value.sheyingzhanghao!=''){
    params['sheyingzhanghao'] = searchQuery.value.sheyingzhanghao
  }
  if(searchQuery.value.sfsh && searchQuery.value.sfsh!=''){
    params['sfsh'] = searchQuery.value.sfsh
  }
  if(searchQuery.value.ispay && searchQuery.value.ispay!=''){
    params['ispay'] = searchQuery.value.ispay
  }
  context.$http({
    url: `${tableName}/page`,
    method: 'get',
    params: params
  }).then(res => {
    listLoading.value = false
    list.value = res.data.data.list
    total.value = Number(res.data.data.total)
  })
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
}
//列表数据
//分页
const total = ref(0)
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
//权限验证
const btnAuth = (e,a)=>{
  return context?.$toolUtil.isAuth(e,a)
}
const shChange = async (type,row)=>{
  searchClick()
}
//搜索
const sheyingzhanghaoLists = ref([])
const getsheyingzhanghaoLists = () => {
  context.$http({
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
//表单
const formRef = ref(null)
const formModelChange=()=>{
  searchClick()
}
const addClick = ()=>{
  formRef.value.init()
}
const editClick = (id=null)=>{
  if(id){
    formRef.value.init(id,'edit')
    return
  }
  if(selRows.value.length){
    formRef.value.init(selRows.value[0].id,'edit')
  }
}

const infoClick = (id=null)=>{
  if(id){
    formRef.value.init(id,'info')
  }
  else if(selRows.value.length){
    formRef.value.init(selRows.value[0].id,'info')
  }
}
// 表单
// 预览文件
const preClick = (file) =>{
  if(!file){
    context?.$toolUtil.message('文件不存在','error')
  }
  window.open(context?.$config.url + file)
}
// 下载文件
const download = (file) => {
  if(!file){
    context?.$toolUtil.message('文件不存在','error')
  }
  let arr = file.replace(new RegExp('file/', "g"), "")
  axios.get((location.href.split(context?.$config.name).length>1 ? location.href.split(context?.$config.name)[0] :'') + context?.$config.name + '/file/download?fileName=' + arr, {
    headers: {
      token: context?.$toolUtil.storageGet('Token')
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
import payForm from '@/components/common/payForm'
const pay = ref(null)
const payClick = (row)=>{
  if(row.sfsh!='是'){
    context?.$toolUtil.message("请审核通过后再操作",'error')
    return false
  }
  pay.value.payClick(tableName,row)
}
//审核
import Approval from '@/components/common/approval.vue'
const approvalRef = ref(null)
const approvalClick = (row) => {
  let params = {
    id:row.id,
    sheyingzhanghao: row.sheyingzhanghao,
    sheyingxingming: row.sheyingxingming,
    yuyuejiage: row.yuyuejiage,
    sfsh: row.sfsh,
    shhf: row.shhf,
    yonghuzhanghao: row.yonghuzhanghao,
    yonghuxingming: row.yonghuxingming,
    yuyuesheyingriqi: row.yuyuesheyingriqi, yuyueshijianduan: row.yuyueshijianduan, lianxidianhua: row.lianxidianhua,
    ispay: row.ispay,
    crossuserid: row.crossuserid,
    crossrefid: row.crossrefid,
  }
  nextTick(() => {
    approvalRef.value.approvalClick(params )
  })
}
//查询审核状态列表
const approvalLists = ref([])
//初始化
const init = () => {
  if (window.location.hash.includes('pending=1')) searchQuery.value.sfsh = '待审核';
  approvalLists.value = "是,否,待审核".split(',');
  getList()
}
init()
</script>
<style lang="scss" scoped>

// 操作盒子
.list_search_view {
  // 搜索盒子
  .search_form {
    // 子盒子
    .search_view {
      // 搜索label
      .search_label {
      }
      // 搜索item
      .search_box {
        // 下拉框
        :deep(.search_sel) {
          //去掉默认样式
          .select-trigger{
            height: 100%;
            .el-input{
              height: 100%;
            }
          }
        }
      }
    }
    // 搜索按钮盒子
    .search_btn_view {
      // 搜索按钮
      .search_btn {
      }
      // 搜索按钮-悬浮
      .search_btn:hover {
      }
    }
  }
  //头部按钮盒子
  .btn_view {
    // 其他
    :deep(.el-button--default){
    }
    // 其他-悬浮
    :deep(.el-button--default:hover){
    }
    // 新增
    :deep(.el-button--success){
    }
    // 新增-悬浮
    :deep(.el-button--success:hover){
    }
    // 删除
    :deep(.el-button--danger){
    }
    // 删除-悬浮
    :deep(.el-button--danger:hover){
    }
    // 统计
    :deep(.el-button--warning){
    }
    // 统计-悬浮
    :deep(.el-button--warning:hover){
    }
  }
}
// 表格样式
.el-table {
  :deep(.el-table__header-wrapper) {
    thead {
      tr {
        th {
          .cell {
          }
        }
      }
    }
  }
  :deep(.el-table__body-wrapper) {
    tbody {
      tr {
        td {
          .cell {
            // 编辑
            .el-button--primary {
            }
            // 编辑-悬浮
            .el-button--primary:hover {
            }
            // 详情
            .el-button--info {
            }
            // 详情-悬浮
            .el-button--info:hover {
            }
            // 删除
            .el-button--danger {
            }
            // 删除-悬浮
            .el-button--danger:hover {
            }
            // 跨表
            .el-button--success {
            }
            // 跨表-悬浮
            .el-button--success:hover {
            }
            // 操作
            .el-button--warning {
            }
            // 操作-悬浮
            .el-button--warning:hover {
            }
          }
        }
      }
      tr:hover {
        td {
        }
      }
    }
  }
}
// 分页器
.el-pagination {
  // 总页码
  :deep(.el-pagination__total) {
    margin: 0 10px 0 0;
    color: #666;
    font-weight: 400;
    display: inline-block;
    vertical-align: top;
    font-size: 13px;
    line-height: 28px;
    height: 28px;
  }
  // 上一页
  :deep(.btn-prev) {
    border: none;
    border-radius: 2px;
    padding: 0;
    margin: 0 5px;
    color: #666;
    background: #f4f4f5;
    display: inline-block;
    vertical-align: top;
    font-size: 13px;
    line-height: 28px;
    min-width: 35px;
    height: 28px;
  }
  // 下一页
  :deep(.btn-next) {
    border: none;
    border-radius: 2px;
    padding: 0;
    margin: 0 5px;
    color: #666;
    background: #f4f4f5;
    display: inline-block;
    vertical-align: top;
    font-size: 13px;
    line-height: 28px;
    min-width: 35px;
    height: 28px;
  }
  // 上一页禁用
  :deep(.btn-prev:disabled) {
    border: none;
    cursor: not-allowed;
    border-radius: 2px;
    padding: 0;
    margin: 0 5px;
    color: #C0C4CC;
    background: #f4f4f5;
    display: inline-block;
    vertical-align: top;
    font-size: 13px;
    line-height: 28px;
    height: 28px;
  }
  // 下一页禁用
  :deep(.btn-next:disabled) {
    border: none;
    cursor: not-allowed;
    border-radius: 2px;
    padding: 0;
    margin: 0 5px;
    color: #C0C4CC;
    background: #f4f4f5;
    display: inline-block;
    vertical-align: top;
    font-size: 13px;
    line-height: 28px;
    height: 28px;
  }
  // 页码
  :deep(.el-pager) {
    padding: 0;
    margin: 0;
    display: inline-block;
    vertical-align: top;
    // 数字
    .number {
      cursor: pointer;
      padding: 0 4px;
      margin: 0 5px;
      color: #666;
      display: inline-block;
      vertical-align: top;
      font-size: 13px;
      line-height: 28px;
      border-radius: 2px;
      background: #f4f4f5;
      text-align: center;
      min-width: 30px;
      height: 28px;
    }
    // 数字悬浮
    .number:hover {
      cursor: pointer;
      padding: 0 4px;
      margin: 0 5px;
      color: #409EFF;
      display: inline-block;
      vertical-align: top;
      font-size: 13px;
      line-height: 28px;
      border-radius: 2px;
      background: #f4f4f5;
      text-align: center;
      min-width: 30px;
      height: 28px;
    }
    // 选中
    .number.is-active {
      cursor: default;
      padding: 0 4px;
      margin: 0 5px;
      color: #FFF;
      display: inline-block;
      vertical-align: top;
      font-size: 13px;
      line-height: 28px;
      border-radius: 2px;
      background: #409EFF;
      text-align: center;
      min-width: 30px;
      height: 28px;
    }
  }
  // sizes
  :deep(.el-pagination__sizes) {
    display: inline-block;
    vertical-align: top;
    font-size: 13px;
    line-height: 28px;
    height: 28px;
    .el-select {
      border: 1px solid #DCDFE6;
      cursor: pointer;
      padding: 0;
      color: #606266;
      display: inline-block;
      font-size: 13px;
      line-height: 28px;
      border-radius: 3px;
      outline: 0;
      background: #FFF;
      width: 100%;
      text-align: center;
      height: 28px;
    }
  }
  // 跳页
  :deep(.el-pagination__jump) {
    margin: 0 0 0 24px;
    color: #606266;
    display: inline-block;
    vertical-align: top;
    font-size: 13px;
    line-height: 28px;
    height: 28px;
    // 输入框
    .el-input {
      border: 1px solid #DCDFE6;
      cursor: pointer;
      padding: 0 3px;
      color: #606266;
      display: inline-block;
      font-size: 14px;
      line-height: 28px;
      border-radius: 3px;
      outline: 0;
      background: #FFF;
      width: 100%;
      text-align: center;
      height: 28px;
    }
  }
}
</style>
