<template>
  <div>
    <el-dialog modal-class="edit_form_modal" class="edit_form" v-model="formVisible" :title="formTitle" width="min(920px, 94vw)" destroy-on-close :fullscreen='false'>
      <el-form label-position="top" class="formModel_form" ref="formRef" :model="form" :rules="rules">
        <el-row >
          <el-col :xs="24" :sm="12">
            <el-form-item label="摄影账号" prop="sheyingzhanghao">
              <el-select
                  class="list_sel"
                  :disabled="!isAdd||disabledForm.sheyingzhanghao?true:false"
                  v-model="form.sheyingzhanghao"
                  placeholder="请选择摄影账号"
                  @change="sheyingzhanghaoChange">
                <el-option v-for="(item,index) in sheyingzhanghaoLists" :label="item" :value="item">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12">
            <el-form-item label="摄影姓名" prop="sheyingxingming">
              <el-input class="list_inp" v-model="form.sheyingxingming" placeholder="摄影姓名"
                        type="text"
                        :readonly="!isAdd||disabledForm.sheyingxingming?true:false" />
            </el-form-item>
          </el-col>

          <el-col :xs="24" :sm="12">
            <el-form-item label="预约价格" prop="yuyuejiage">
              <el-input class="list_inp" v-model.number="form.yuyuejiage" placeholder="预约价格"
                        type="number"
                        @mousewheel.native.prevent
                        :readonly="!isAdd||disabledForm.yuyuejiage?true:false" />
            </el-form-item>
          </el-col>

          <el-col :xs="24" :sm="12">
            <el-form-item label="用户账号" prop="yonghuzhanghao">
              <el-input class="list_inp" v-model="form.yonghuzhanghao" placeholder="用户账号"
                        type="text"
                        :readonly="!isAdd||disabledForm.yonghuzhanghao?true:false" />
            </el-form-item>
          </el-col>

          <el-col :xs="24" :sm="12">
            <el-form-item label="用户姓名" prop="yonghuxingming">
              <el-input class="list_inp" v-model="form.yonghuxingming" placeholder="用户姓名"
                        type="text"
                        :readonly="!isAdd||disabledForm.yonghuxingming?true:false" />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12">
            <el-form-item label="用户电话" prop="lianxidianhua">
              <el-input class="list_inp" v-model="form.lianxidianhua" placeholder="用户姓名"
                        type="text"
                        :readonly="!isAdd||disabledForm.lianxidianhua?true:false" />
            </el-form-item>
          </el-col>

          <el-col :xs="24" :sm="12">
            <el-form-item label="预约摄影日期" prop="yuyuesheyingriqi">
              <el-date-picker
                  class="list_date"
                  v-model="form.yuyuesheyingriqi"
                  format="YYYY-MM-DD"
                  value-format="YYYY-MM-DD"
                  type="date"
                  :readonly="!isAdd||disabledForm.yuyuesheyingriqi?true:false"
                  placeholder="请选择预约摄影日期" />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12">
            <el-form-item label="选择时间段" prop="yuyueshijianduan">
              <el-select
                  class="list_sel"
                  v-model="form.yuyueshijianduan"
                  placeholder="请选择预约摄影时间段"
                  style="width:100%;"
                  :readonly="!isAdd||disabledForm.yuyueshijianduan?true:false"
                  :disabled="!form.yuyuesheyingriqi"
              >
                <el-option
                    v-for="option in timeOptions"
                    :key="option.value"
                    :label="option.label"
                    :value="option.value"
                    :disabled="isOptionDisabled(option.value)"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer v-if="isAdd||type=='logistics'||type=='reply'">
				<span class="formModel_btn_box">
					<el-button class="cancel_btn" @click="closeClick">取消</el-button>
					<el-button class="confirm_btn" type="primary" @click="save"
          >
						提交
					</el-button>
				</span>
      </template>
    </el-dialog>
  </div>
</template>
<script setup>
import {
  reactive,
  ref,
  getCurrentInstance,
  nextTick,
  computed,
  defineEmits
} from 'vue'
import {
  useStore
} from 'vuex';
const store = useStore()
const user = computed(()=>store.getters['user/session'])
const context = getCurrentInstance()?.appContext.config.globalProperties;
const emit = defineEmits(['formModelChange'])
//基础信息
const tableName = 'sheyingshiyuyue'
const formName = '摄影师预约'
//基础信息
//form表单
const form = ref({})
const disabledForm = ref({
  sheyingzhanghao : false,
  sheyingxingming : false,
  yuyuejiage : false,
  sfsh : false,
  shhf : false,
  yonghuzhanghao : false,
  yonghuxingming : false,
  yuyuesheyingriqi : false,
  yuyueshijianduan : false,
  lianxidianhua:false,
  ispay : false,
  crossuserid : false,
  crossrefid : false,
})
// 时间段选项
const timeOptions = [
  { label: "08:00-09:00", value: "08:00-09:00" },
  { label: "09:00-10:00", value: "09:00-10:00" },
  { label: "10:00-11:00", value: "10:00-11:00" },
  { label: "11:00-12:00", value: "11:00-12:00" },
  { label: "13:00-14:00", value: "13:00-14:00" },
  { label: "14:00-15:00", value: "14:00-15:00" },
  { label: "15:00-16:00", value: "15:00-16:00" },
  { label: "16:00-17:00", value: "16:00-17:00" }
]
// 判断选项是否禁用
const isOptionDisabled = (optionValue) => {
  console.log(sheyingshiyuyueList.value,"111111")
  if (!sheyingshiyuyueList.value) return false;
  return sheyingshiyuyueList.value.some(item => item.yuyueshijianduan === optionValue);
};
//日期查询摄影师预约集合
const sheyingshiyuyueList = ref([])
//根据日期查询摄影师的预约情况
const selecteSheyingshiyuyue=()=>{
  let params={
    sheyingzhanghao:form.value.sheyingzhanghao,
    yuyuesheyingriqi:form.value.yuyuesheyingriqi
  }
  context?.$http({
    url: `${tableName}/page`,
    method: 'get',
    params:params
  }).then(res=>{
    console.log(res.data.data.list,"data")
    sheyingshiyuyueList.value = res.data.data.list
  })

}


const formVisible = ref(false)
const isAdd = ref(false)
const formTitle = ref('')

const rules = ref({
  sheyingzhanghao: [
  ],
  sheyingxingming: [
  ],
  yuyuejiage: [
    { validator: context.$toolUtil.validator.number, trigger: 'blur' },
  ],
  sfsh: [
  ],
  shhf: [
  ],
  yonghuzhanghao: [
  ],
  yonghuxingming: [
  ],
  yuyuesheyingriqi: [
  ],
  ispay: [
  ],
  crossuserid: [
  ],
  crossrefid: [
  ],
})
//表单验证

const formRef = ref(null)
const id = ref(0)
const type = ref('')
//摄影账号列表
const sheyingzhanghaoLists = ref([])

//获取唯一标识
const getUUID =()=> {
  return new Date().getTime();
}
//重置
const resetForm = () => {
  form.value = {
    sheyingzhanghao: '',
    sheyingxingming: '',
    yuyuejiage: '',
    sfsh: '',
    shhf: '',
    yonghuzhanghao: '',
    yonghuxingming: '',
    yuyuesheyingriqi: '',
    crossuserid: '',
    crossrefid: '',
  }
}
//获取info
const getInfo = ()=>{
  context?.$http({
    url: `${tableName}/info/${id.value}`,
    method: 'get'
  }).then(res => {
    let reg=new RegExp('../../../file','g')
    form.value = res.data.data
    formVisible.value = true
  })
}
const crossRow = ref('')
const crossTable = ref('')
const crossTips = ref('')
const crossColumnName = ref('')
const crossColumnValue = ref('')
//初始化
const init=(formId=null,formType='add',formNames='',row=null,table=null,statusColumnName=null,tips=null,statusColumnValue=null)=>{
  resetForm()
  if(formId){
    id.value = formId
    type.value = formType
  }
  if(formType == 'add'){
    isAdd.value = true
    formTitle.value = '新增' + formName
    formVisible.value = true
  }else if(formType == 'info'){
    isAdd.value = false
    formTitle.value = '查看' + formName
    getInfo()
  }else if(formType == 'edit'){
    isAdd.value = true
    formTitle.value = '修改' + formName
    getInfo()
  }
  else if(formType == 'cross'){
    isAdd.value = true
    formTitle.value = formNames
    // getInfo()
    for(let x in row){
      if(x=='sheyingzhanghao'){
        form.value.sheyingzhanghao = row[x];
        disabledForm.value.sheyingzhanghao = true;
        continue;
      }
      if(x=='sheyingxingming'){
        form.value.sheyingxingming = row[x];
        disabledForm.value.sheyingxingming = true;
        continue;
      }
      if(x=='yuyuejiage'){
        form.value.yuyuejiage = row[x];
        disabledForm.value.yuyuejiage = true;
        continue;
      }
      if(x=='yonghuzhanghao'){
        form.value.yonghuzhanghao = row[x];
        disabledForm.value.yonghuzhanghao = true;
        continue;
      }
      if(x=='yonghuxingming'){
        form.value.yonghuxingming = row[x];
        disabledForm.value.yonghuxingming = true;
        continue;
      }
      if(x=='yuyuesheyingriqi'){
        form.value.yuyuesheyingriqi = row[x];
        disabledForm.value.yuyuesheyingriqi = true;
        continue;
      }
      if(x=='crossuserid'){
        form.value.crossuserid = row[x];
        disabledForm.value.crossuserid = true;
        continue;
      }
      if(x=='crossrefid'){
        form.value.crossrefid = row[x];
        disabledForm.value.crossrefid = true;
        continue;
      }
    }
    if(row){
      crossRow.value = row
    }
    if(table){
      crossTable.value = table
    }
    if(tips){
      crossTips.value = tips
    }
    if(statusColumnName){
      crossColumnName.value = statusColumnName
    }
    if(statusColumnValue){
      crossColumnValue.value = statusColumnValue
    }
    formVisible.value = true
  }

  context?.$http({
    url: `${context?.$toolUtil.storageGet('sessionTable')}/session`,
    method: 'get'
  }).then(res => {
    var json = res.data.data
    if(json.hasOwnProperty('yonghuzhanghao')&& context?.$toolUtil.storageGet("role")!="管理员"){
      form.value.yonghuzhanghao = json.yonghuzhanghao
      disabledForm.value.yonghuzhanghao = true;
    }
    if(json.hasOwnProperty('yonghuxingming')&& context?.$toolUtil.storageGet("role")!="管理员"){
      form.value.yonghuxingming = json.yonghuxingming
      disabledForm.value.yonghuxingming = true;
    }
  })
  context?.$http({
    url: `option/sheyingshi/sheyingzhanghao`,
    method: 'get'
  }).then(res=>{
    sheyingzhanghaoLists.value = res.data.data
  })
  //由上级字段带出不可改
  disabledForm.value.sheyingxingming = true;
}
//初始化
//声明父级调用
defineExpose({
  init
})
//关闭
const closeClick = () => {
  formVisible.value = false
}
const sheyingzhanghaoChange=()=>{
  context?.$http({
    url: `follow/sheyingshi/sheyingzhanghao?columnValue=` + form.value.sheyingzhanghao,
    method: 'get'
  }).then(res=>{
    if(res.data.data.sheyingxingming){
      form.value.sheyingxingming = res.data.data.sheyingxingming
    }
  })
}
//富文本
const editorChange = (e,name) =>{
  form.value[name] = e
}
//提交
const save= async ()=>{
  var table = crossTable.value
  var objcross = JSON.parse(JSON.stringify(crossRow.value))
  let crossUserId = ''
  let crossRefId = ''
  let crossOptNum = ''
  if(type.value == 'cross'){
    if(crossColumnName.value!=''){
      if(!crossColumnName.value.startsWith('[')){
        for(let o in objcross){
          if(o == crossColumnName.value){
            objcross[o] = crossColumnValue.value
          }
        }
        //修改跨表数据
        changeCrossData(objcross)
      }else{
        crossUserId = user.value.id
        crossRefId = objcross['id']
        crossOptNum = crossColumnName.value.replace(/\[/,"").replace(/\]/,"")
      }
    }
  }
  formRef.value.validate((valid)=>{
    if(valid){
      if(crossUserId&&crossRefId){
        form.value.crossuserid = crossUserId
        form.value.crossrefid = crossRefId
        let params = {
          page: 1,
          limit: 1000,
          crossuserid:form.value.crossuserid,
          crossrefid:form.value.crossrefid,
        }
        context?.$http({
          url: `${tableName}/page`,
          method: 'get',
          params: params
        }).then(res=>{
          if(res.data.data.total>=crossOptNum){
            context?.$toolUtil.message(`${crossTips.value}`,'error')
            return false
          }else{
            context?.$http({
              url: `${tableName}/${!form.value.id ? "save" : "update"}`,
              method: 'post',
              data: form.value
            }).then(async res=>{
              emit('formModelChange')
              context?.$toolUtil.message(`操作成功`,'success')
              formVisible.value = false
            })
          }
        })
      }else{
        context?.$http({
          url: `${tableName}/${!form.value.id ? "save" : "update"}`,
          method: 'post',
          data: form.value
        }).then(async (res)=>{
          emit('formModelChange')
          context?.$toolUtil.message(`操作成功`,'success')
          formVisible.value = false
        })
      }
    }else{
      context.$message.error('请完善信息')
    }
  })
}
//修改跨表数据
const changeCrossData = async (row)=>{
  if(type.value == 'cross'){
    await context?.$http({
      url: `${crossTable.value}/update`,
      method: 'post',
      data: row
    }).then(res=>{})
  }
}
</script>
<style lang="scss" scoped>
// 表单
.formModel_form{
  // form item
  :deep(.el-form-item) {
    //label
    .el-form-item__label {
    }
    // 内容盒子
    .el-form-item__content {
      // 输入框
      .list_inp {
      }
      //日期选择器
      .list_date {
      }
      // 下拉框
      .list_sel {
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
}
// 按钮盒子
.formModel_btn_box {
  .cancel_btn {
  }
  .cancel_btn:hover {
  }

  .confirm_btn {
  }
  .confirm_btn:hover {
  }
}
</style>
