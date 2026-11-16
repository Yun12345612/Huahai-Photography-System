<template>
	<div>
		<div class="login_view">
			<el-form :model="loginForm" class="login_form">
				<div class="title_view">
                    花海 · 工作人员入口
                </div>
				<p class="login_subtitle">管理员和摄影师请从这里登录，系统会根据角色显示对应工作台。</p><div class="list_item" v-if="loginType==1">
					<div class="list_label">
						账号：
					</div>
					<input class="list_inp" v-model="loginForm.username" placeholder="请输入账号" name="username" />
				</div>
				<div class="list_item" v-if="loginType==1">
					<div class="list_label">
						密码：
					</div>
					<input class="list_inp" v-model="loginForm.password" type="password" placeholder="请输入密码" @keydown.enter.native="handleLogin"  />
				</div>
				<div class="list_type" v-if="userList.length>1">
					<div class="list_label">
						工作角色：
					</div>
				  <el-select v-model="loginForm.role" placeholder="请选择管理员或摄影师">
				    <el-option v-for="(item,index) in userList" :label="item.roleName" :value="item.roleName"></el-option>
				  </el-select>
				</div>
				<div class="photographer-apply"><a href="http://localhost:8083/#/sheyingshiRegister">没有摄影师账号？申请注册</a></div>
<div class="btn_view">
					<el-button class="login" v-if="loginType==1" type="success" @click="handleLogin">登录</el-button>
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
	} from "vue";
	import { useStore } from 'vuex'
	const store = useStore()
	const userList = ref([])
	const menus = ref([])
	const loginForm = ref({
		role: '',
		username: '',
		password: ''
	})
	const tableName = ref('')
	const loginType = ref(1)
	const context = getCurrentInstance()?.appContext.config.globalProperties;
	const handleLogin = () => {
		if (!loginForm.value.username) {
			context?.$toolUtil.message('请输入用户名', 'error')
			
			return;
		}
		if (!loginForm.value.password) {
			context?.$toolUtil.message('请输入密码', 'error')
			return;
		}
		if (userList.value.length > 1) {
			if (!loginForm.value.role) {
				context?.$toolUtil.message('请选择角色', 'error')
				
				return;
			}
			for (let i = 0; i < menus.value.length; i++) {
				if (menus.value[i].roleName == loginForm.value.role) {
					tableName.value = menus.value[i].pathName||menus.value[i].tableName;
				}
			}
		} else {
			tableName.value = userList.value[0].pathName||userList.value[0].tableName;
			loginForm.value.role = userList.value[0].roleName;
		}
		login()
	}
	const login = () => {
		context?.$http({
			url: `${tableName.value}/login?username=${loginForm.value.username}&password=${loginForm.value.password}`,
			method: 'post'
		}).then(res => {
			context?.$toolUtil.storageSet("Token", res.data.token);
			context?.$toolUtil.storageSet("role", loginForm.value.role);
			context?.$toolUtil.storageSet("sessionTable", tableName.value);
			context?.$toolUtil.storageSet("adminName", loginForm.value.username);
            //vuex中获取用户信息并保存
			store.dispatch('user/getSession').then(res=>{
                context?.$router.push('/')
            })
		}, err => {
		})
	}
	//获取菜单
	const getMenu=()=> {
      let params = {
        page: 1,
        limit: 1,
        sort: 'id',
      }

      context?.$http({
        url: "menu/list",
        method: "get",
        params: params
      }).then(res => {
          menus.value = JSON.parse(res.data.data.list[0].menujson)
          for (let i = 0; i < menus.value.length; i++) {
            if (menus.value[i].hasBackLogin=='是') {
              userList.value.push(menus.value[i])
            }
          }
			loginForm.value.role = userList.value[0].roleName
          context?.$toolUtil.storageSet("menus", JSON.stringify(menus.value));
      })
    }
	//初始化
	const init = () => {
		getMenu();
	}
	onMounted(()=>{
		init()
		

	})
</script>



<style scoped>.login_view{min-height:100vh;display:flex;align-items:center;justify-content:center;background:linear-gradient(120deg,#213e30 0%,#315942 45%,#edf1e6 45%);padding:32px}.login_form{width:420px;max-width:100%;padding:46px;background:#fafbf7;border:1px solid #e2e8d9;border-radius:10px;box-shadow:0 24px 80px #122e2520}.login_subtitle{margin:-22px 0 30px;color:#7d8b78;font-size:13px;line-height:1.8}.title_view{font-size:25px;color:#305d4c;letter-spacing:2px;margin-bottom:36px}.list_item,.list_type{margin-bottom:22px}.list_label{font-size:12px;color:#829174;margin-bottom:9px}.list_inp{width:100%;box-sizing:border-box;height:44px;padding:0 12px;border:1px solid #dfe6d5;border-radius:4px;background:white;font-size:14px;color:#405b37}.list_type :deep(.el-select){width:100%}.photographer-apply{text-align:right;margin:-8px 0 8px}.photographer-apply a{color:#4a8f72;font-size:12px;text-decoration:none}.login.el-button{width:100%;height:44px;background:#305d4c;color:#fff;border:0;border-radius:4px;margin-top:12px}@media(max-width:600px){.login_view{padding:24px;background:#edf1e6}.login_form{padding:32px 24px}}</style>
