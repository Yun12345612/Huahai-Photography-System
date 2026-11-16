<template>
	<div>
		<div class="register_view"><aside class="register-story"><router-link to="/index/home">花海摄影工作室</router-link><div><small>YOUR STORY STARTS HERE</small><h1>新的故事，<br/>从认识你开始。</h1><p>加入花海，遇见懂你的摄影师。</p></div></aside>
			<el-form :model="registerForm" class="register_form">
				<div class="title_view">
                    创建你的花海账号
                </div>
				<div class="list_item">
					<div class="list_label">用户账号：</div>
					<input class="list_inp"
					 v-model="registerForm.yonghuzhanghao" 
					 placeholder="请输入用户账号"
					 type="text"
					 />
				</div>
				<div class="list_item">
					<div class="list_label">密码：</div>
					<input class="list_inp"
					 v-model="registerForm.mima" 
					 placeholder="请输入密码"
					 type="password"
					 />
				</div>
				<div class="list_item">
					<div class="list_label">确认密码：</div>
					<input class="list_inp" v-model="registerForm.mima2" type="password" placeholder="请输入确认密码" />
				</div>
				<div class="list_item">
					<div class="list_label">用户姓名：</div>
					<input class="list_inp"
					 v-model="registerForm.yonghuxingming" 
					 placeholder="请输入用户姓名"
					 type="text"
					 />
				</div>
				<div class="list_item">
					<div class="list_label">头像：</div>
					<div class="list_file_list">
						<uploads
							action="file/upload" 
							tip="请上传头像"
							style="width: 100%;text-align: left;"
							:fileUrls="registerForm.touxiang?registerForm.touxiang:''" 
							@change="touxiangUploadSuccess">
						</uploads>
					</div>
				</div>
				<div class="list_item">
					<div class="list_label">性别：</div>
					<el-select 
						class="list_sel" 
						v-model="registerForm.xingbie" 
						placeholder="请选择性别"
						>
						<el-option v-for="item in yonghuxingbieLists" :label="item" :value="item"></el-option>
					</el-select>
				</div>
				<div class="list_item">
					<div class="list_label">联系电话：</div>
					<input class="list_inp"
					 v-model="registerForm.lianxidianhua" 
					 placeholder="请输入联系电话"
					 type="text"
					 />
				</div>
				<div class="list_btn">
					<el-button class="register" type="success" @click="handleRegister">注册</el-button>
					<div class="r-login" @click="close">已有账号，直接登录</div>
				</div>
			</el-form>	
		</div>
	</div>
</template>
<script setup>
	import {
		ref,
		getCurrentInstance,
		nextTick,
		onMounted,
	} from 'vue';
	const context = getCurrentInstance()?.appContext.config.globalProperties;
	const projectName = context?.$project.projectName
	//获取注册类型
	import { useRoute } from 'vue-router';
	const route = useRoute()
	const tableName = ref('yonghu')

	//公共方法
	const getUUID=()=> {
		return new Date().getTime();
	}
	
	const registerForm = ref({
        xingbie: '',
	})
	const yonghuxingbieLists = ref([])
	const init=()=>{
		yonghuxingbieLists.value = "男,女".split(',')
	}
    const touxiangUploadSuccess=(fileUrls)=> {
        registerForm.value.touxiang = fileUrls;
    }
	// 多级联动参数
	//注册按钮
	const handleRegister = () => {
		let url = tableName.value +"/register";
		if((!registerForm.value.yonghuzhanghao)){
			context?.$toolUtil.message(`用户账号不能为空`,'error')
			return false
		}
		if((!registerForm.value.mima)){
			context?.$toolUtil.message(`密码不能为空`,'error')
			return false
		}
		if(registerForm.value.mima!=registerForm.value.mima2){
			context?.$toolUtil.message('两次密码输入不一致','error')
			return false
		}
		if((!registerForm.value.yonghuxingming)){
			context?.$toolUtil.message(`用户姓名不能为空`,'error')
			return false
		}
		if(registerForm.value.touxiang!=null){
			registerForm.value.touxiang = registerForm.value.touxiang.replace(new RegExp(context?.$config.url,"g"),"");
		}
		if(registerForm.value.lianxidianhua&&(!context?.$toolUtil.isMobile(registerForm.value.lianxidianhua))){
			context?.$toolUtil.message(`联系电话应输入手机格式`,'error')
			return false
		}
		context?.$http({
			url:url,
			method:'post',
			data:registerForm.value
		}).then(res=>{
			context?.$toolUtil.message('注册成功','success', obj=>{
				context?.$router.push({
					path: "/login"
				});
			})
		})
	}
	//返回登录
	const close = () => {
		context?.$router.push({
			path: "/login"
		});
	}
	init()
	onMounted(()=>{

	})
</script>


<style scoped>
.register_view{--el-color-primary:#305d4c;min-height:100vh;box-sizing:border-box;position:relative;padding:55px 6vw 45px 57%;background:#faf9f6;color:#294735;display:flex;align-items:center;justify-content:center}.register-story{position:absolute;inset:0 50% 0 0;background:linear-gradient(145deg,#375e49,#172f27);padding:48px;display:flex;flex-direction:column;justify-content:space-between;color:white;overflow:hidden}.register-story:after{content:'';position:absolute;width:650px;height:650px;border:1px solid #ffffff18;border-radius:50%;top:22%;right:-260px;pointer-events:none}.register-story a{color:white;text-decoration:none;font-size:23px;letter-spacing:3px}.register-story>div{padding-bottom:90px;z-index:1}.register-story small{font-size:10px;letter-spacing:3px;color:#c6d5bf}.register-story h1{font-family:SimSun,serif;font-size:clamp(32px,3.4vw,52px);font-weight:400;line-height:1.6;letter-spacing:4px}.register-story p{font-size:13px;color:#cbd9c3}.register_form{width:100%;max-width:430px;background:none;padding:12px 0;display:block}.title_view{font-size:28px;font-weight:400;letter-spacing:2px;margin-bottom:30px;color:#294735}.list_item{margin-bottom:17px}.list_label{font-size:12px;line-height:1.8;margin-bottom:7px;color:#728264}.list_inp{box-sizing:border-box;width:100%;height:42px;padding:0 12px;border:1px solid #dfe5d7;border-radius:4px;background:white;color:#344b39;font:inherit;font-size:13px;outline:none}.list_inp:focus{border-color:#6d885d;box-shadow:0 0 0 2px #305d4c10}.register_form :deep(.el-select){width:100%}.register_form :deep(.el-upload--picture-card),.register_form :deep(.el-upload-list__item){width:64px;height:64px;line-height:64px}.register_form :deep(.el-upload__tip){font-size:11px;color:#8c987f;line-height:1.7}.register.el-button{width:100%;height:44px;background:#305d4c;color:white;border:0;border-radius:4px;letter-spacing:2px;margin-top:10px}.register:hover{background:#244b3a}.r-login{font-size:12px;color:#677f58;text-align:center;margin-top:22px;cursor:pointer}@media(max-width:800px){.register-story{display:none}.register_view{padding:40px 24px}.register_form{max-width:430px}.title_view{font-size:25px}}
</style>
