<template>
	<div>
		<el-dialog modal-class="edit_form_modal" class="edit_form" v-model="formVisible" :title="formTitle" width="min(920px, 94vw)" destroy-on-close :fullscreen='false'>
			<el-form label-position="top" class="formModel_form" ref="formRef" :model="form" :rules="rules">
				<el-row >
					<el-col :xs="24" :sm="12">
						<el-form-item label="作品名称" prop="zuopinmingcheng">
							<el-input class="list_inp" v-model="form.zuopinmingcheng" placeholder="作品名称"
                                type="text"
								:readonly="!isAdd||disabledForm.zuopinmingcheng?true:false" />
						</el-form-item>
					</el-col>

					<el-col :xs="24" :sm="12">
						<el-form-item label="摄影账号" prop="sheyingzhanghao">
							<el-input class="list_inp" v-model="form.sheyingzhanghao" placeholder="摄影账号"
                                type="text"
								:readonly="!isAdd||disabledForm.sheyingzhanghao?true:false" />
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
						<el-form-item label="作品简介" prop="zuopinjianjie">
							<el-input class="list_inp" v-model="form.zuopinjianjie" placeholder="作品简介"
                                type="text"
								:readonly="!isAdd||disabledForm.zuopinjianjie?true:false" />
						</el-form-item>
					</el-col>

					<el-col :xs="24" :sm="12">
						<el-form-item prop="zuopintupian"
									  label="作品图片"
						>
							<uploads
								:disabled="!isAdd||disabledForm.zuopintupian?true:false"
								action="file/upload"
								tip="请上传作品图片"
								style="width: 100%;text-align: left;"
								:fileUrls="form.zuopintupian?form.zuopintupian:''" 
								@change="zuopintupianUploadSuccess">
							</uploads>
						</el-form-item>
					</el-col>
					<el-col :xs="24" :sm="12">
						<el-form-item label="发布时间" prop="fabushijian">
							<el-date-picker
								class="list_date"
								v-model="form.fabushijian"
								format="YYYY-MM-DD HH:mm:ss"
								value-format="YYYY-MM-DD HH:mm:ss"
								type="datetime"
								:readonly="!isAdd||disabledForm.fabushijian?true:false"
								placeholder="请选择发布时间" />
						</el-form-item>
					</el-col>
					<el-col :span="24">
						<el-form-item label="作品概述" prop="zuopingaishu">
							<el-input v-model="form.zuopingaishu" placeholder="作品概述" type="textarea"
							:readonly="!isAdd||disabledForm.zuopingaishu?true:false"
							/>
						</el-form-item>
					</el-col>
					<el-col :span="24">
						<el-form-item label="创作背景" prop="chuangzuobeijing">
							<el-input v-model="form.chuangzuobeijing" placeholder="创作背景" type="textarea"
							:readonly="!isAdd||disabledForm.chuangzuobeijing?true:false"
							/>
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
	const tableName = 'sheyingzuopin'
	const formName = '摄影作品'
	//基础信息
	//form表单
	const form = ref({})
	const disabledForm = ref({
        zuopinmingcheng : false,
        sheyingzhanghao : false,
        sheyingxingming : false,
        zuopinjianjie : false,
        zuopintupian : false,
        zuopingaishu : false,
        chuangzuobeijing : false,
        fabushijian : false,
	})
	const formVisible = ref(false)
	const isAdd = ref(false)
	const formTitle = ref('')
    
	const rules = ref({
		zuopinmingcheng: [
		],
		sheyingzhanghao: [
		],
		sheyingxingming: [
		],
		zuopinjianjie: [
		],
		zuopintupian: [
		],
		zuopingaishu: [
		],
		chuangzuobeijing: [
		],
		fabushijian: [
		],
	})
	//表单验证
	
	const formRef = ref(null)
	const id = ref(0)
	const type = ref('')
	//作品图片上传回调
	const zuopintupianUploadSuccess=(e)=>{
		form.value.zuopintupian = e
	}

	//获取唯一标识
	const getUUID =()=> {
      return new Date().getTime();
    }
	//重置
	const resetForm = () => {
		form.value = {
			zuopinmingcheng: '',
			sheyingzhanghao: '',
			sheyingxingming: '',
			zuopinjianjie: '',
			zuopintupian: '',
			zuopingaishu: '',
			chuangzuobeijing: '',
			fabushijian: '',
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
			form.value.fabushijian = context?.$toolUtil.getCurDateTime()
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
				if(x=='zuopinmingcheng'){
					form.value.zuopinmingcheng = row[x];
					disabledForm.value.zuopinmingcheng = true;
					continue;
				}
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
				if(x=='zuopinjianjie'){
					form.value.zuopinjianjie = row[x];
					disabledForm.value.zuopinjianjie = true;
					continue;
				}
				if(x=='zuopintupian'){
					form.value.zuopintupian = row[x];
					disabledForm.value.zuopintupian = true;
					continue;
				}
				if(x=='zuopingaishu'){
					form.value.zuopingaishu = row[x];
					disabledForm.value.zuopingaishu = true;
					continue;
				}
				if(x=='chuangzuobeijing'){
					form.value.chuangzuobeijing = row[x];
					disabledForm.value.chuangzuobeijing = true;
					continue;
				}
				if(x=='fabushijian'){
					form.value.fabushijian = row[x];
					disabledForm.value.fabushijian = true;
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
			if(json.hasOwnProperty('sheyingzhanghao')&& context?.$toolUtil.storageGet("role")!="管理员"){
				form.value.sheyingzhanghao = json.sheyingzhanghao
				disabledForm.value.sheyingzhanghao = true;
			}
			if(json.hasOwnProperty('sheyingxingming')&& context?.$toolUtil.storageGet("role")!="管理员"){
				form.value.sheyingxingming = json.sheyingxingming
				disabledForm.value.sheyingxingming = true;
			}
		})
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
	//富文本
	const editorChange = (e,name) =>{
		form.value[name] = e
	}
	//提交
	const save= async ()=>{
		if(form.value.zuopintupian!=null) {
			form.value.zuopintupian = form.value.zuopintupian.replace(new RegExp(context?.$config.url,"g"),"");
		}
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
				// 长文本
				.el-textarea__inner {
				}
				//图片上传样式
				.el-upload-list  {
					//提示语
					.el-upload__tip {
					}
					//外部盒子
					.el-upload--picture-card {
						//图标
						.el-icon{
						}
					}
					.el-upload-list__item {
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
