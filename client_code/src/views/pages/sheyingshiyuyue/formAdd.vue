<template>
  <div class="edit_view appointment-page">
    <header class="appointment-heading"><span>BOOK YOUR SESSION</span><h1>预约一段光影时光</h1><p>选择摄影师与空闲档期，提交后请等待工作室确认。</p></header>
    <div class="appointment-steps"><span>01 选择摄影师</span><span>02 选择日期与时段</span><span>03 确认联系方式</span></div>
    <div class="breadcrumb-wrapper" style="width: 100%;">
      <div class="bread_view">
        <el-breadcrumb separator="" class="breadcrumb">
          <el-breadcrumb-item class="first_breadcrumb" :to="{ path: '/' }">首页</el-breadcrumb-item>
          <el-breadcrumb-item class="second_breadcrumb" v-for="(item,index) in breadList" :key="index">{{item.name}}</el-breadcrumb-item>
        </el-breadcrumb>
      </div>
    </div>
    <el-form ref="formRef" :model="form" class="add_form" label-width="120px" :rules="rules">
      <el-row>
        <el-col :xs="24" :sm="12">
          <el-form-item label="选择摄影师" prop="sheyingzhanghao">
            <el-select
                class="list_sel"
                :disabled="!isAdd||disabledForm.sheyingzhanghao?true:false"
                v-model="form.sheyingzhanghao"
                placeholder="请选择摄影师"
                style="width:100%;"
                @change="sheyingzhanghaoChange">
              <el-option v-for="(item,index) in sheyingzhanghaoLists" :label="photographerNames[item] || item" :value="item">
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12">
          <el-form-item label="摄影姓名" prop="sheyingxingming">
            <el-input class="list_inp"
                      v-model="form.sheyingxingming"
                      placeholder="摄影姓名"
                      type="text"
                      :readonly="!isAdd||disabledForm.sheyingxingming?true:false" />
          </el-form-item>
        </el-col>

        <el-col :xs="24" :sm="12">
          <el-form-item label="预约价格" prop="yuyuejiage">
            <el-input class="list_inp"
                      v-model.number="form.yuyuejiage"
                      placeholder="预约价格"
                      type="number"
                      :readonly="!isAdd||disabledForm.yuyuejiage?true:false" />
          </el-form-item>
        </el-col>

        <el-col :xs="24" :sm="12">
          <el-form-item label="用户账号" prop="yonghuzhanghao">
            <el-input class="list_inp"
                      v-model="form.yonghuzhanghao"
                      placeholder="用户账号"
                      type="text"
                      :readonly="!isAdd||disabledForm.yonghuzhanghao?true:false" />
          </el-form-item>
        </el-col>

        <el-col :xs="24" :sm="12">
          <el-form-item label="用户姓名" prop="yonghuxingming">
            <el-input class="list_inp"
                      v-model="form.yonghuxingming"
                      placeholder="用户姓名"
                      type="text"
                      :readonly="!isAdd||disabledForm.yonghuxingming?true:false" />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12">
          <el-form-item label="用户电话" prop="lianxidianhua">
            <el-input class="list_inp"
                      v-model="form.lianxidianhua"
                      placeholder="请输入11位手机号码"
                      type="tel"
                      maxlength="11"
                      :readonly="!isAdd" />
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
                style="width:100%;"
                :readonly="!isAdd || disabledForm.yuyuesheyingriqi ? true : false"
                placeholder="请选择预约摄影日期"
                :disabled-date="disabledDateFun"
                @change="selecteSheyingshiyuyue()">
            </el-date-picker>
          </el-form-item>
        </el-col>

        <el-col :xs="24" :sm="12">
          <el-form-item label="选择时间段" prop="yuyueshijianduan">
            <el-select
                class="list_sel"
                v-model="form.yuyueshijianduan"
                placeholder="请选择预约摄影时间段"
                style="width:100%;"
                :disabled="!form.yuyuesheyingriqi || !form.sheyingzhanghao || availabilityLoading || availabilityError"
            >
              <el-option
                  v-for="option in timeOptions"
                  :key="option.value"
                  :label="option.label"
                  :value="option.value"
                  :disabled="isOptionDisabled(option.value)"
              ></el-option>
            </el-select>
            <p class="availability-note" role="status">{{ availabilityLoading ? '正在查询可预约时段…' : availabilityError ? '档期查询失败，请重试后选择时段' : '已预约或已开始的时段不可选择' }}</p>
            <el-button v-if="availabilityError" text @click="selecteSheyingshiyuyue">重新查询</el-button>
          </el-form-item>
        </el-col>

      </el-row>
      <el-alert title="预约须知" type="info" :closable="false" show-icon description="提交预约后，请在个人中心查看审核进度。价格以工作室服务信息为准；需要调整已确认的预约，请通过在线咨询联系工作室。" />
      <div class="formModel_btn_box">
        <el-button class="formModel_cancel" @click="backClick">取消</el-button>
        <el-button class="formModel_confirm"
                   @click="save"
                   :loading="submitting"
                   :disabled="availabilityLoading || availabilityError"
                   type="success"
        >
          {{ form.id ? '保存修改' : '提交预约' }}
        </el-button>
      </div>
    </el-form>
  </div>
</template>
<script setup>
import {
  ref,
  getCurrentInstance,
  watch,
  onUnmounted,
  onMounted,
  nextTick,
  computed
} from 'vue';
import {
  useRoute,
  useRouter
} from 'vue-router';
import moment from "moment";
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
//获取唯一标识
const getUUID =()=> {
  return new Date().getTime();
}
//form表单
const form = ref({
  sheyingzhanghao: '',
  sheyingxingming: '',
  yuyuejiage: 0,
  sfsh: '',
  shhf: '',
  yonghuzhanghao: '',
  yonghuxingming: '',
  lianxidianhua: '',
  yuyuesheyingriqi: '',
  yuyueshijianduan:'',
  crossuserid: '',
  crossrefid: '',
})
const formRef = ref(null)
const id = ref(0)
const type = ref('')
const disabledForm = ref({
  sheyingzhanghao : false,
  sheyingxingming : false,
  yuyuejiage : false,
  sfsh : false,
  shhf : false,
  yonghuzhanghao : false,
  yonghuxingming : false,
  lianxidianhua : false,
  yuyuesheyingriqi : false,
  ispay : false,
  crossuserid : false,
  crossrefid : false,
})
const isAdd = ref(false)
//表单验证
const rules = ref({
  sheyingzhanghao: [{ required: true, message: '请选择摄影师', trigger: 'change' }],
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
  lianxidianhua: [{ required: true, message: '请填写联系电话', trigger: 'blur' }, { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的11位手机号码', trigger: 'blur' }],
  yuyuesheyingriqi: [
    {required: true,message: '请选择预约摄影日期',trigger: 'blur'}
  ],
  yuyueshijianduan: [
    {required: true,message: '请选择预约时间段',trigger: 'change'}
  ],
  ispay: [
  ],
  crossuserid: [
  ],
  crossrefid: [
  ],
})
//摄影账号列表
const sheyingzhanghaoLists = ref([])
const photographerNames = ref({})
const submitting = ref(false)
context.$http.get('sheyingshixinxi/list', { params: { page: 1, limit: 1000, sfsh: '是' } }).then(res => {
  photographerNames.value = Object.fromEntries(res.data.data.list.map(person => [person.sheyingzhanghao, person.sheyingxingming])); sheyingzhanghaoLists.value = Object.keys(photographerNames.value)
})
//methods

//methods
//获取info
const getInfo = ()=>{
  context?.$http({
    url: `${tableName}/info/${id.value}`,
    method: 'get'
  }).then(res => {
    let reg=new RegExp('../../../file','g')
    form.value = res.data.data
    originalBooking.value = { ...res.data.data }
  })
}
const crossRow = ref('')
const crossTable = ref('')
const crossTips = ref('')
const crossColumnName = ref('')
const crossColumnValue = ref('')
//初始化
const init = (formId=null,formType='add',formNames='',row=null,table=null,statusColumnName=null,tips=null,statusColumnValue=null) => {
  if(formId){
    id.value = formId
    type.value = formType
  }
  if(formType == 'add'){
    isAdd.value = true
  }else if(formType == 'info'){
    isAdd.value = false
    getInfo()
  }else if(formType == 'edit'){
    isAdd.value = true
    getInfo()
  }
  else if(formType == 'cross'){
    isAdd.value = true
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
      if(x=='lianxidianhua'){
        form.value.lianxidianhua = row[x];
        disabledForm.value.lianxidianhua = true;
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
  }
  context?.$http({
    url: `${context?.$toolUtil.storageGet('frontSessionTable')}/session`,
    method: 'get'
  }).then(res => {
    var json = res.data.data
    if(json.hasOwnProperty('yonghuzhanghao') && context?.$toolUtil.storageGet("frontRole")!="管理员"){
      form.value.yonghuzhanghao = json.yonghuzhanghao
      disabledForm.value.yonghuzhanghao = true;
    }
    if(json.hasOwnProperty('yonghuxingming') && context?.$toolUtil.storageGet("frontRole")!="管理员"){
      form.value.yonghuxingming = json.yonghuxingming
      disabledForm.value.yonghuxingming = true;
    }
    if(json.hasOwnProperty('lianxidianhua') && context?.$toolUtil.storageGet("frontRole")!="管理员"){
      form.value.lianxidianhua = json.lianxidianhua
      disabledForm.value.lianxidianhua = true;
    }
    if (localStorage.getItem('autoSave')) {
      localStorage.removeItem('autoSave')
      save()
    }
  })
  //由上级字段带出不可改
  disabledForm.value.sheyingxingming = true;
}
//初始化
//取消
const backClick = () => {
  history.back()
}
//摄影账号回调
const sheyingzhanghaoChange=async ()=>{
  form.value.yuyueshijianduan = ''
  try {
    const result = await context.$http.get('sheyingshixinxi/list', { params: { page: 1, limit: 1, sheyingzhanghao: form.value.sheyingzhanghao, sfsh: '是' } })
    const person = result.data.data.list[0]
    form.value.sheyingxingming = person?.sheyingxingming || ''
    form.value.yuyuejiage = person?.yuyuejiage || 0
    disabledForm.value.yuyuejiage = true
  } catch { form.value.sheyingxingming = ''; form.value.yuyuejiage = 0 }
}
//提交
const save=async ()=>{
  if (submitting.value) return
  if (availabilityLoading.value || availabilityError.value || isOptionDisabled(form.value.yuyueshijianduan)) {
    context.$toolUtil.message('请选择有效的空闲时段', 'error'); return
  }
  submitting.value = true
  try {
    await formRef.value.validate()
    await context.$http.post(`${tableName}/${form.value.id ? 'update' : 'save'}`, form.value)
    context.$toolUtil.message(form.value.id ? '预约已更新' : '预约已提交，请等待工作室确认', 'success')
    router.push({ path: '/index/sheyingshiyuyueList', query: { centerType: 1 } })
  } catch (error) {
    if (error?.data?.code === 409) await selecteSheyingshiyuyue()
  } finally { submitting.value = false }
}
// Availability contains only occupied slot names, never customer details.
const timeOptions = ['08:00-09:00','09:00-10:00','10:00-11:00','11:00-12:00','13:00-14:00','14:00-15:00','15:00-16:00','16:00-17:00'].map(value => ({label:value,value}))
const occupiedSlots = ref([])
const availabilityLoading = ref(false)
const availabilityError = ref(false)
let availabilityRequest = 0
const isOptionDisabled = value => {
  if (!value) return false
  const currentOwnSlot = form.value.id && originalBooking.value?.yuyuesheyingriqi === form.value.yuyuesheyingriqi && originalBooking.value?.sheyingzhanghao === form.value.sheyingzhanghao && originalBooking.value?.yuyueshijianduan === value
  return (!currentOwnSlot && occupiedSlots.value.includes(value)) || new Date(`${form.value.yuyuesheyingriqi}T${value.slice(0,5)}:00+08:00`).getTime() <= Date.now()
}
const originalBooking = ref(null)
const selecteSheyingshiyuyue = async () => {
  const sequence = ++availabilityRequest
  occupiedSlots.value = []; availabilityError.value = false
  if (!form.value.yuyuesheyingriqi || !form.value.sheyingzhanghao) { availabilityLoading.value = false; return }
  availabilityLoading.value = true
  try {
    const result = await context.$http.get(`${tableName}/availability`, { params: { sheyingzhanghao: form.value.sheyingzhanghao, date: form.value.yuyuesheyingriqi } })
    if (sequence !== availabilityRequest) return
    occupiedSlots.value = result.data.data
    if (isOptionDisabled(form.value.yuyueshijianduan)) form.value.yuyueshijianduan = ''
  } catch (error) {
    if (sequence === availabilityRequest) { availabilityError.value = true; form.value.yuyueshijianduan = '' }
  } finally { if (sequence === availabilityRequest) availabilityLoading.value = false }
}
watch(() => [form.value.sheyingzhanghao, form.value.yuyuesheyingriqi], selecteSheyingshiyuyue)
const disabledDateFun = time => {
  const chinaDay = new Intl.DateTimeFormat('en-CA', { timeZone: 'Asia/Shanghai', year: 'numeric', month: '2-digit', day: '2-digit' }).format(new Date())
  const selectedDay = `${time.getFullYear()}-${String(time.getMonth()+1).padStart(2,'0')}-${String(time.getDate()).padStart(2,'0')}`
  return selectedDay < chinaDay
}
//获取当前系统时间
const getFormattedDate=()=> {
  const now = new Date();
  const year = now.getFullYear();
  const month = (now.getMonth() + 1).toString().padStart(2, '0');
  const day = now.getDate().toString().padStart(2, '0');
  return `${year}-${month}-${day}`;
}
onMounted(async ()=>{
  if (!context.$toolUtil.storageGet('frontToken')) { context.$toolUtil.storageSet('toPath',route.fullPath); router.replace('/login'); return }
  if (route.query.photographerId) {
    try {
      const result = await context.$http.get('sheyingshixinxi/detail/' + encodeURIComponent(route.query.photographerId))
      const person = result.data.data
      if (!person || person.sfsh !== '是') { context.$toolUtil.message('该摄影师当前不可预约','warning'); router.replace('/index/sheyingshixinxiList'); return }
      form.value.sheyingzhanghao=person.sheyingzhanghao;form.value.sheyingxingming=person.sheyingxingming;form.value.yuyuejiage=person.yuyuejiage
      disabledForm.value.sheyingzhanghao=true;disabledForm.value.yuyuejiage=true
    } catch { router.replace('/index/sheyingshixinxiList'); return }
  }

  type.value = route.query.type?route.query.type:'add'
  let row = null
  let table = null
  let statusColumnName = null
  let tips = null
  let statusColumnValue = null
  if(type.value == 'cross'){
    row = context?.$toolUtil.storageGet('crossObj')?JSON.parse(context?.$toolUtil.storageGet('crossObj')):{}
    table = context?.$toolUtil.storageGet('crossTable')
    statusColumnName = context?.$toolUtil.storageGet('crossStatusColumnName')
    tips = context?.$toolUtil.storageGet('crossTips')
    statusColumnValue = context?.$toolUtil.storageGet('crossStatusColumnValue')
  }
  init(route.query.id?route.query.id:null, type.value,'', row, table, statusColumnName, tips, statusColumnValue)
})
onUnmounted(()=>{
  Object.keys(localStorage).map(item=>{
    if(item.startsWith('cross')){
      localStorage.removeItem(item)
    }
  })
})
</script>
<style lang="scss" scoped>
// 面包屑盒子
.bread_view {
  :deep(.breadcrumb) {
    .el-breadcrumb__separator {
    }
    .first_breadcrumb {
      .el-breadcrumb__inner {
      }
    }
    .second_breadcrumb {
      .el-breadcrumb__inner {
      }
    }
  }
}
// 表单
.add_form{
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
  .formModel_cancel {
  }
  .formModel_cancel:hover {
  }

  .formModel_confirm {
  }
  .formModel_confirm:hover {
  }
}
</style>
<style lang="scss">
.edit_view {
  margin: 20px auto;
  width: 1400px;
  position: relative;
  background: rgb(255, 255, 255);
  font-size:16px;
  color:#666;
}
.edit_view .add_form{
  width: 100%;
  padding: 30px;
  border:1px solid #eee;
}
.edit_view .add_form .el-form-item{
  margin: 0px 0px 20px;
  display: flex;
}
.edit_view .add_form .el-form-item .el-form-item__label{
  width: 150px;
  background: none;
  text-align: right;
  display: block;
  font-size: 16px;
  color: rgb(51, 51, 51);
  font-weight: 500;
}
.edit_view .add_form .el-form-item .el-form-item__content{
  display: flex;
  justify-content: flex-start;
  align-items: center;
  flex-wrap: wrap;
  width: calc(100% - 120px);
}
.edit_view .add_form .el-form-item .el-form-item__content .list_inp{
  height: 36px;
  line-height: 36px;
  border: 1px solid rgba(226, 227, 229, 1);
  padding: 0px 10px;
  width: 100%;
  box-sizing: border-box;
  background: rgb(255, 255, 255);
  font-size: 16px;
}

.edit_view .add_form .el-form-item .el-form-item__content .list_sel{
  line-height: 36px;

  box-sizing: border-box;
  width: calc(100% - 120px);
  padding: 0px 10px;
  border-radius: 0px;
  background: rgb(255, 255, 255);
  font-size: 16px;
}

.edit_view .add_form .el-form-item .el-form-item__content .list_date{
  line-height: 36px;
  border: 1px solid rgba(226, 227, 229, 1);
  box-sizing: border-box;
  width: 100%;
  border-radius: 0px;
  background: rgb(255, 255, 255);
  font-size: 16px;
}








</style>
<style lang="scss">
.home .appointment-page{box-sizing:border-box;max-width:1100px;width:calc(100% - 48px);margin:32px auto 60px;padding:0;background:#faf9f6}.appointment-heading{text-align:center;padding:26px 16px}.appointment-heading>span{font-size:11px;letter-spacing:3px;color:#74836f}.appointment-heading h1{font-size:32px;font-weight:400;letter-spacing:3px;color:#294a3b;margin:16px 0}.appointment-heading p{font-size:14px;color:#7a8376;line-height:1.8}.appointment-steps{display:flex;justify-content:center;gap:40px;color:#56734f;font-size:13px;padding:20px;border-bottom:1px solid #e1e6dc}.home .appointment-page .add_form{box-sizing:border-box;width:100%;margin:0;padding:36px 32px;background:#fff;border:1px solid #e5e9df;border-top:0;border-radius:0 0 8px 8px}.appointment-page .breadcrumb-wrapper{display:none}.appointment-page .el-form-item{margin-bottom:26px}.appointment-page .el-form-item__label{color:#5d6957}.appointment-page .availability-note{font-size:12px;color:#7b8475;line-height:1.6;margin:6px 0 0}.appointment-page .formModel_btn_box{display:flex;justify-content:flex-end;gap:12px;padding:28px 0 0}.appointment-page .el-alert{line-height:1.8}.appointment-page .formModel_confirm.el-button{background:#305d4c;border-color:#305d4c;color:#fff;border-radius:4px;min-width:130px}.appointment-page .formModel_cancel.el-button{border-radius:4px}@media(max-width:640px){.home .appointment-page{width:calc(100% - 24px);margin-top:12px}.home .appointment-page .add_form{padding:24px 12px}.appointment-steps{gap:12px;font-size:11px}.appointment-heading h1{font-size:26px}.appointment-page .el-form-item__label{width:100px!important}.appointment-page .el-form-item__content{margin-left:0!important}}
</style>
