<template>
	<div class="edit_view" :style='{}'>
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
				<el-col :span="12">
					<el-form-item label="摄影账号" prop="sheyingzhanghao">
						<el-input class="list_inp"
                                  v-model="form.sheyingzhanghao"
                                  placeholder="摄影账号"
                                  type="text"
							      :readonly="!isAdd||disabledForm.sheyingzhanghao?true:false" />
					</el-form-item>
				</el-col>

				<el-col :span="12">
					<el-form-item label="摄影姓名" prop="sheyingxingming">
						<el-input class="list_inp"
                                  v-model="form.sheyingxingming"
                                  placeholder="摄影姓名"
                                  type="text"
							      :readonly="!isAdd||disabledForm.sheyingxingming?true:false" />
					</el-form-item>
				</el-col>

				<el-col :span="12">
					<el-form-item label="摄影师照片" prop="sheyingshizhaopian">
						<uploads
							:disabled="!isAdd||disabledForm.sheyingshizhaopian?true:false"
							action="file/upload" 
							tip="请上传摄影师照片"
							style="width: 100%;text-align: left;"
							:fileUrls="form.sheyingshizhaopian?form.sheyingshizhaopian:''" 
							@change="sheyingshizhaopianUploadSuccess">
						</uploads>
					</el-form-item>
				</el-col>
				<el-col :span="12">
					<el-form-item label="分类" prop="fenlei">
						<el-select
							class="list_sel"
							:disabled="!isAdd||disabledForm.fenlei?true:false"
							v-model="form.fenlei" 
							placeholder="请选择分类"
							style="width:100%;"
							>
							<el-option v-for="(item,index) in fenleiLists" :label="item"
								:value="item"
								>
							</el-option>
						</el-select>
					</el-form-item>
				</el-col>
				<el-col :span="12">
					<el-form-item label="简介" prop="jianjie">
						<el-input class="list_inp"
                                  v-model="form.jianjie"
                                  placeholder="简介"
                                  type="text"
							      :readonly="!isAdd||disabledForm.jianjie?true:false" />
					</el-form-item>
				</el-col>

				<el-col :span="12">
					<el-form-item label="预约价格" prop="yuyuejiage">
						<el-input class="list_inp"
                                  v-model.number="form.yuyuejiage"
                                  placeholder="预约价格"
                                  type="number"
							      :readonly="!isAdd||disabledForm.yuyuejiage?true:false" />
					</el-form-item>
				</el-col>

				<el-col :span="24">
					<el-form-item label="工作经历" prop="gongzuojingli">
						<el-input v-model="form.gongzuojingli" placeholder="工作经历" type="textarea"
						:readonly="!isAdd||disabledForm.gongzuojingli?true:false"
						/>
					</el-form-item>
				</el-col>
				<el-col :span="24">
					<el-form-item label="专业技能" prop="zhuanyejineng">
						<el-input v-model="form.zhuanyejineng" placeholder="专业技能" type="textarea"
						:readonly="!isAdd||disabledForm.zhuanyejineng?true:false"
						/>
					</el-form-item>
				</el-col>
				<el-col :span="24">
					<el-form-item label="个人特长" prop="gerentezhang">
						<el-input v-model="form.gerentezhang" placeholder="个人特长" type="textarea"
						:readonly="!isAdd||disabledForm.gerentezhang?true:false"
						/>
					</el-form-item>
				</el-col>
			</el-row>
			<div class="formModel_btn_box">
				<el-button class="formModel_cancel" @click="backClick">取消</el-button>
				<el-button class="formModel_confirm"
                           @click="save"
                           type="success"
				>
					保存
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
	const tableName = 'sheyingshixinxi'
	const formName = '摄影师信息'
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
		sheyingshizhaopian: '',
		fenlei: '',
		jianjie: '',
		yuyuejiage: 0,
		gongzuojingli: '',
		zhuanyejineng: '',
		gerentezhang: '',
		shhf: '',
	})
	const formRef = ref(null)
	const id = ref(0)
	const type = ref('')
	const disabledForm = ref({
		sheyingzhanghao : false,
		sheyingxingming : false,
		sheyingshizhaopian : false,
		fenlei : false,
		jianjie : false,
		yuyuejiage : false,
		gongzuojingli : false,
		zhuanyejineng : false,
		gerentezhang : false,
		sfsh : false,
		shhf : false,
		discussNumber : false,
	})
	const isAdd = ref(false)
	//表单验证
	const rules = ref({
		sheyingzhanghao: [
		],
		sheyingxingming: [
		],
		sheyingshizhaopian: [
		],
		fenlei: [
		],
		jianjie: [
		],
		yuyuejiage: [
			{ validator: context.$toolUtil.validator.number, trigger: 'blur' },
		],
		gongzuojingli: [
		],
		zhuanyejineng: [
		],
		gerentezhang: [
		],
		sfsh: [
		],
		shhf: [
		],
		discussNumber: [
			{ validator: context.$toolUtil.validator.intNumber, trigger: 'blur' },
		],
	})
	//摄影师照片上传回调
	const sheyingshizhaopianUploadSuccess=(e)=>{
		form.value.sheyingshizhaopian = e
	}
	//分类列表
	const fenleiLists = ref([])
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
				if(x=='sheyingshizhaopian'){
					form.value.sheyingshizhaopian = row[x];
					disabledForm.value.sheyingshizhaopian = true;
					continue;
				}
				if(x=='fenlei'){
					form.value.fenlei = row[x];
					disabledForm.value.fenlei = true;
					continue;
				}
				if(x=='jianjie'){
					form.value.jianjie = row[x];
					disabledForm.value.jianjie = true;
					continue;
				}
				if(x=='yuyuejiage'){
					form.value.yuyuejiage = row[x];
					disabledForm.value.yuyuejiage = true;
					continue;
				}
				if(x=='gongzuojingli'){
					form.value.gongzuojingli = row[x];
					disabledForm.value.gongzuojingli = true;
					continue;
				}
				if(x=='zhuanyejineng'){
					form.value.zhuanyejineng = row[x];
					disabledForm.value.zhuanyejineng = true;
					continue;
				}
				if(x=='gerentezhang'){
					form.value.gerentezhang = row[x];
					disabledForm.value.gerentezhang = true;
					continue;
				}
				if(x=='discussNumber'){
					form.value.discussNumber = row[x];
					disabledForm.value.discussNumber = true;
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
			if(json.hasOwnProperty('sheyingzhanghao') && context?.$toolUtil.storageGet("frontRole")!="管理员"){
				form.value.sheyingzhanghao = json.sheyingzhanghao
				disabledForm.value.sheyingzhanghao = true;
			}
			if(json.hasOwnProperty('sheyingxingming') && context?.$toolUtil.storageGet("frontRole")!="管理员"){
				form.value.sheyingxingming = json.sheyingxingming
				disabledForm.value.sheyingxingming = true;
			}
            if (localStorage.getItem('autoSave')) {
                localStorage.removeItem('autoSave')
                save()
            }
		})
		context?.$http({
			url: `option/fenlei/fenlei`,
			method: 'get'
		}).then(res=>{
			fenleiLists.value = res.data.data
		})
	}
	//初始化
	//取消
	const backClick = () => {
		history.back()
	}
	//提交
	const save=()=>{
		if(form.value.sheyingshizhaopian!=null) {
			form.value.sheyingshizhaopian = form.value.sheyingshizhaopian.replace(new RegExp(context?.$config.url,"g"),"");
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
					crossUserId = context?.$toolUtil.storageGet('userid')
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
							}).then(res=>{
                                context?.$toolUtil.message(`操作成功`,'success')
                                history.back()
							})
						}
					})
				}else{
					context?.$http({
						url: `${tableName}/${!form.value.id ? "save" : "update"}`,
						method: 'post', 
						data: form.value 
					}).then(res=>{
                        context?.$toolUtil.message(`操作成功`,'success')
                        history.back()
					})
				}
			}
		})
	}
	//修改跨表数据
	const changeCrossData=(row)=>{
        if(type.value == 'cross'){
            context?.$http({
                url: `${crossTable.value}/update`,
                method: 'post',
                data: row
            }).then(res=>{})
        }
	}
	onMounted(()=>{
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





.edit_view .add_form .el-form-item .el-form-item__content .el-textarea{border: 1px solid rgba(226, 227, 229, 1);}
.edit_view .add_form .el-form-item .el-form-item__content .el-textarea .el-textarea__inner{
    width: 100%;
    min-height: 150px;
    padding: 12px;
    border: 1px solid rgba(255, 255, 255, 1);
    border-radius: 0px;
    color: #666;
    font-size: 16px;
}


.edit_view .add_form .el-form-item .el-form-item__content .el-upload--picture-card{
    background-color: rgb(255, 255, 255);
    width: 100px;
    height: 90px;
    line-height: 100px;
    text-align: center;
    
    border-radius: 0px;
    cursor: pointer;
}

.edit_view .add_form .el-form-item .el-form-item__content .el-upload--picture-card .el-icon{
    font-size: 32px;
    color: #999;
}

.edit_view .add_form .el-form-item .el-form-item__content .img-uploader .el-upload__tip{
    font-size: 15px;
    color: #666;
    margin: 0;
}



</style>