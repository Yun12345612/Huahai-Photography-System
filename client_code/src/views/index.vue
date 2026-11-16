<template>
	<div class="home">
		<div class="index_top">
			<div class="index_top_title"><img class="huahai-brand-mark" :src="brandLogo" alt="花海摄影 Logo"/><span>花海摄影工作室</span></div>
            <div class="menu-wrapper">
                <el-scrollbar wrap-class="scrollbar-wrapper" class="menu_scrollbar">
                    <el-menu :default-openeds="[]" :unique-opened="true" :default-active="menuIndex"
                         class="menu_view" mode="horizontal" :ellipsis="false" @select="menuChange" :key="menuIndex">
                        <el-menu-item class="first-item" index="/index/home" @click="menuHandler('/')">
                            <i class="iconfont icon-zhuye2"></i>
                            <template #title>
                                <span>首页</span>
                            </template>
                        </el-menu-item>
                        <template v-for="(menu,index) in menuList" :key="menu.menu">
                            <el-sub-menu v-if="menu.child.length>1" :index="menu.name" class="first-item" :teleported="true">
                                <template #title>
                                    <i class="iconfont" :class="menu.icon"></i>
                                    <span>{{ menu.name }}</span>
                                </template>
                                <el-menu-item class="second-item" v-for=" (child,index1) in menu.child" :key="index1"
                                              :index="child.url" @click="menuHandler(child.url)">{{ child.name }}
                                </el-menu-item>
                            </el-sub-menu>
                            <el-menu-item v-else :index="menu.child[0].url" class="first-item" @click="menuHandler(menu.child[0].url)">
                                <i class="iconfont" :class="menu.icon"></i>
                                <template #title>
                                    <span>{{menu.child[0].name}}</span>
                                </template>
                            </el-menu-item>
                        </template>
                    </el-menu>
                </el-scrollbar>
            </div>
			<div class="index_top_right">
				<div class="chat" style="cursor: pointer;position: relative;" @click="chatClick">
					<i class="iconfont icon-kefu2"></i>
					<span>在线咨询</span>
				</div>
				<el-button v-if="!Token" class="login" type="success" icon="User" @click="loginClick">
					登录
				</el-button>
				<MessageBadge v-if="Token"/><div class="user" v-if="Token">
					<el-dropdown class="avatar-container" trigger="hover">
						<div class="avatar-wrapper">
							<img class="user-avatar" :src="store.getters['user/avatar']" style="width: 50px">
							<div class="nickname">{{store.getters['user/username']}}</div>
							<el-icon class="el-icon-arrow-down">
								<arrow-down />
							</el-icon>
						</div>
						<template #dropdown>
							<el-dropdown-menu class="user-dropDown" slot="dropdown">
								<el-dropdown-item @click="menuHandler('messageCenter')"><span>消息中心</span></el-dropdown-item>
								<el-dropdown-item @click="menuHandler('center')" class="center">
									<span>个人中心</span>
								</el-dropdown-item>
								<el-dropdown-item @click="loginOut" class="loginOut">
									<span>退出登录</span>
								</el-dropdown-item>
							</el-dropdown-menu>
						</template>
					</el-dropdown>
				</div>
			</div>
		</div>
		<el-scrollbar class="contain_view">
			<div class="rotation_view" v-if="false">
				<mySwiper :type="3" :data="rotationList" :autoHeight="false" :autoplay="true"
					:loop="false" :navigation="false" :pagination="false"
					:paginationType="1" :scrollbar="false" :slidesPerView="1"
					:spaceBetween="20" :centeredSlides="false"
					:freeMode="false" :effectType="0"
					:direction="horizontal">
					<template #default="scope">
						<img :style='{}' :src="scope.row.value?$config.url + scope.row.value:''" @click="carouselClick(scope.row.url)">
					</template>
				</mySwiper>
			</div>
			<MemberWorkspace v-if="route.path.endsWith('Center') || route.query.centerType"><router-view :key="routerKey"/></MemberWorkspace><router-view v-else :key="routerKey"/>
			<el-backtop :right="100" :bottom="100" />
            <footer class="studio-footer"><div><strong>花海摄影工作室</strong><p>以光为笔，记录每一个值得珍藏的瞬间。</p></div><div><span>拍摄预约</span><button @click="menuHandler('sheyingshixinxiList')">了解摄影师与服务 ↗</button><button @click="chatClick">在线咨询 ↗</button></div><small>HUAHAI PHOTO STUDIO · 留住此刻的美好</small></footer>
		</el-scrollbar>
        <OnlineConsultation ref="consultationRef" />
	</div>
</template>
<script setup>
	import menu from '@/utils/menu'
import MemberWorkspace from '@/components/MemberWorkspace.vue'
import MessageBadge from '@/components/MessageBadge.vue'
    import OnlineConsultation from '@/components/OnlineConsultation.vue'
	import axios from 'axios'
    import moment from "moment";
	import {
		ref,
		onBeforeUnmount,
		getCurrentInstance,
		nextTick,
		computed,
		watch,
        provide
	} from 'vue';
	import {
		useRouter,
		useRoute
	} from 'vue-router';
	import {
		useStore
	} from 'vuex';
	const brandLogo = process.env.BASE_URL + 'brand/huahai-logo.svg'
	const store = useStore()
	const router = useRouter()
	const route = useRoute();
	//用于获取当前组件实例.appContext.config.globalProperties 用于访问 Vue 应用实例的全局属性
	const context = getCurrentInstance()?.appContext.config.globalProperties;
	const Token = ref('')
	const dateTimer = ref(null)
    const nowDate = ref(new Date())
	if(localStorage.getItem('frontToken') && !store.getters['user/session'].id){
		store.dispatch('user/getSession').catch(() => { Token.value = context?.$toolUtil.storageGet('frontToken') || '' })
	}
	onBeforeUnmount(() => {
		clearInterval(dateTimer.value)

	})
	// 获取默认菜单index
	const setMenuIndex = (path)=>{
        if(path){
            menuIndex.value = path.replace('Detail','List')
        }else{
            menuIndex.value = ''
        }
	}
	// 默认菜单index
	const menuIndex = ref('')
    const routerKey = ref(Math.random())
	watch(() => router.currentRoute.value,(value, oldValue) => {
		Token.value = context?.$toolUtil.storageGet('frontToken')
        if(value.path==oldValue?.path){
            //改变路由query时重新加载router-view
            routerKey.value = Math.random()
        }
        if(value.query.centerType){
            setMenuIndex()
        }else{
            setMenuIndex(value.path)
        }
	},{
		immediate:true
	})
	const init = () => {
		// 获取菜单
		getMenu()
		// 赋值token
		Token.value = context?.$toolUtil.storageGet('frontToken')
		// 时间
		dateTimer.value = setInterval(() => {
            nowDate.value = new Date()
		}, 1000)
		// 轮播图
		getRotationList()
		if(Token.value){
			getSession()
		}
	}
	// 切换菜单保存index
	const menuChange = (e)=>{
		if(e=='chat')return
	}
	// 轮播图
	const rotationList = ref([])
	const baseUrl = context?.$config.url
	const getRotationList = () => {
		context?.$http({
			url: 'config/list',
			method: 'get',
			params: {
				page: 1,
				limit: 3
			}
		}).then(res => {
			rotationList.value = res.data.data.list
		}).catch(() => {})
	}

    // 轮播图跳转
    const carouselClick = (url)=>{
        if (url) {
            if (url.startsWith('http')) {
                window.open(url)
            } else {
                context.$router.push(url)
            }
        }
    }
	const menuList = ref([])
	const role = ref('')
	const getMenu = () => {
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
			context?.$toolUtil.storageSet("menus", res.data.data.list[0].menujson);
		}).catch(() => {})
		menuList.value = context?.$config.menuList
	}
	const loginClick = () => {
		context?.$toolUtil.storageSet('toPath',window.history.state.current)
		router.push('/login')
	}
	const loginOut = () => {
		context?.$toolUtil.message('退出成功', 'success')
		context?.$toolUtil.storageClear()
		router.replace('/index/home')
		Token.value = ''

	}
    const consultationRef = ref(null)
    const chatClick = () => consultationRef.value?.open()
	//菜单跳转
	const menuHandler = (name) => {
		if(name=='chat')return chatClick()
		if (name == 'center') {
			name = `${context?.$toolUtil.storageGet('frontSessionTable')}Center`
			menuChange('center')
		}
		router.push(name.startsWith('/') ? name : '/index/' + name)
	}
	// 获取用户信息
	const getSession = () =>{
		context?.$http({
			url: `${context?.$toolUtil.storageGet('frontSessionTable')}/session`,
			method:'get'
		}).then(res=>{
			context?.$toolUtil.storageSet('userid',res.data.data.id)
			if(context?.$toolUtil.storageGet('frontSessionTable') == 'yonghu'){
				context?.$toolUtil.storageSet("frontName", res.data.data.yonghuzhanghao)
			}
			if(context?.$toolUtil.storageGet('frontSessionTable') == 'yonghu'){
				context?.$toolUtil.storageSet('headportrait',res.data.data.touxiang||'')
			}
			if(context?.$toolUtil.storageGet('frontSessionTable') == 'sheyingshi'){
				context?.$toolUtil.storageSet("frontName", res.data.data.sheyingzhanghao)
			}
			if(context?.$toolUtil.storageGet('frontSessionTable') == 'sheyingshi'){
				context?.$toolUtil.storageSet('headportrait',res.data.data.touxiang||'')
			}
		}).catch(() => {})
	}
	init()
</script>
<style lang="scss" scoped>
	/* 头部 */
	.index_top {
		.index_top_title {
		}
		.index_top_right {
			.weather_time_view {
				.weather {
					.city {
					}
					.wea {
					}
				}
				.time {
					.date {
					}
					.timer {
					}
				}
			}
		}
		// 登录按钮
		:deep(.el-button--success) {
		}
		:deep(.el-button--success:hover) {
		}
		// 登出按钮
		:deep(.el-button--danger) {
		}
		:deep(.el-button--danger:hover) {
		}
	}
	// 轮播盒子
	.rotation_view {
	}
	// 底部
	.bottom_view {
		// 图片
		.bottom_img {
			display: none;
		}
		// 公司
		.bottom_company {
		}
		// 备案号
		.bottom_record {
		}
		// 联系方式
		.bottom_desc {
		}
	}
	
	
	

	.contain_view {
		padding: 210px 0 0;
	}
	.el-aside {
		transition: width 0.15s;
		-webkit-transition: width 0.15s;
		-moz-transition: width 0.15s;
		-webkit-transition: width 0.15s;
		-o-transition: width 0.15s;
	}
	.el-sub-menu__hide-arrow{
		display: block !important;
	}
	.menu_scrollbar {
	}
	// 总盒子
	.menu_view {
		// 一级菜单
		:deep(.first-item) {

			// 图标
			.iconfont {
			}
		
			// 标题
			.el-sub-menu__title,
			span {
			}
		
			//箭头
			.el-sub-menu__icon-arrow {
			}
		}
		// 选中
		:deep(.is-active) {
		}
		
		// 悬浮
		:deep(.first-item:hover) {
		}
	}
	// 聊天总盒子
	.chat_form {
		// 聊天内容盒子
		.chat-content {
			// 聊天时间
			.chat_time{
			}
			// 提问盒子
			.left-content {
				// 文字样式
				:deep(.el-alert--success){
					.el-alert__title {
						font-size: inherit;
					}
				}
				// 聊天图片
				.chat_img {
				}
			}
			// 回复盒子
			.right-content {
				// 文字样式
				:deep(.el-alert--warning){
					.el-alert__title {
						font-size: inherit;
					}
				}
				// 聊天图片
				.chat_img {
				}
			}
		}
		// 输入内容盒子
		.input_box{
			// 输入框
			:deep(.el-textarea__inner) {
			}
			// 操作按钮
			.inp_add {
			}
		}
		// 操作盒子
		.upload_box {
			:deep(.upload-demo) {
				// 上传按钮
				.upload_btn {
				}
				// 上传按钮-悬浮
				.upload_btn:hover {
				}
			}
		}
		// 发送盒子
		.save_box {
			// 发送按钮
			.save_btn {
			}
			// 发送按钮-悬浮
			.save_btn:hover {
			}
		}
		.clear-float {
			clear: both;
		}
	}
</style>
<style lang="scss">
	// 二级
	.menu_popper {
		border:none !important;
		background: none !important;

		// 二级总盒子
		.el-menu--popup {
		}

		// 二级菜单
		.second-item {
		}
		
		// 选中
		.is-active {
		}
		
		// 悬浮
		.second-item:hover {
		}
	}
</style>
<style lang="scss">

.index_top {
    width: 100%;
    display: flex;
    flex-wrap: wrap;
    align-items: center;
    background: #fff;
    justify-content: space-between;
    box-sizing: border-box;
    z-index: 1002;
    position: fixed;
    border-bottom: 0px solid #edeef0;
    
    overflow: hidden;
    color: rgba(66, 66, 66, 1);
}

.index_top .index_top_title{
    font-size: 24px;
    font-weight: bold;
    order: 0;
    line-height: 80px;
    order: 2;
    padding-left: calc(50% - 700px);
    width: 100%;
    display: flex;
    align-items: center;
}
.index_top .index_top_title span{
    background: url(http://clfile.zggen.cn/20250521/73dc4588b3814dffbfe10df65e9d03b6.png) no-repeat left center / auto 40px;
    padding:0 0 0 58px;
}


.index_top .index_top_right{
    display: flex;
    align-items: center;
    column-gap: 20px;
    width: 100%;
    background: #fff;
    padding: 18px calc(50% - 700px);
    justify-content: flex-end;
}

.index_top .notice-btn{
    background: var(--theme);
    padding: 0px 6px;
    font-size: 16px;
    border-radius: 0;
    color: rgba(243, 245, 247, 1);
    order: 1;
    border: none;
    height: 30px;
    border-radius: 4px;
}
.index_top .notice-btn:hover{
    color: var(--theme);
}

.index_top .notice-btn .iconfont{
    font-size: 16px;
}


.index_top .index_top_right .chat{
    
    order: 3;
}
.index_top .index_top_right .chat:hover{
    color: var(--theme);
}
.index_top .index_top_right .chat .iconfont{
    font-size: 16px;
}
.index_top .index_top_right .chat span{
    font-size: 16px;
}

.index_top .index_top_right .cart{
    font-size: 16px;
    
    order: 2;
}
.index_top .index_top_right .cart:hover{
    color: var(--theme);
}
.index_top .index_top_right .cart .iconfont{
    font-size: 16px;
    margin-right:5px;
}


.index_top .user .avatar-wrapper{
    display: flex;
    align-items: center;
    cursor:pointer;
    color: inherit;
}

.index_top .user .avatar-wrapper .user-avatar{
    width: 36px !important;
    height: 36px;
    border-radius: 100%;
    margin-right:5px;
}

.index_top .user .avatar-wrapper .nickname{
    font-size: 16px;
    margin-right:5px;
}

.index_top .user .avatar-wrapper .el-icon-arrow-down{
}


.index_top .user-dropDown{
    padding: 10px 0;
    margin: 5px 0;
    background: #fff;
    border: 1px solid #e6ebf5;
    border-radius: 4px;
    -webkit-box-shadow: 0 2px 12px 0 rgba(0,0,0,.1);
    box-shadow: 0 2px 12px 0 rgba(0,0,0,.1);
}
.index_top .user-dropDown li{
    padding: 0 20px !important;
    line-height: 36px !important;
    font-size: 14px !important;
    color: #606266 !important;
}
.index_top .user-dropDown li:hover{
    color: #fff !important;
    background: var(--theme) !important;
}

.index_top .weather_time_view {
    order: 0;
    display: flex;
    align-items: center;
    line-height: 20px;
    font-size: 16px;
    column-gap: 10px;
}

.index_top .login {
    order: 5;
}

.index_top .user {
    order: 5;
}

.index_top .weather {
    display: flex;
    align-items: center;
}

.index_top .time {
    display: flex;
    align-items: center;
    column-gap: 10px;
}

.index_top .login {
    background: var(--theme);
    width: 80px;
    border: none;
    height: 30px;
    border-radius: 4px;
    padding: 0 4px;
}

.index_top .toBack button {
    background: var(--theme);
    color: #fff;
    border-radius: 0;
    border: 0;
    height: 30px;
    padding: 0 6px;
    border-radius: 4px;
}

.index_top .toBack {
    order: 4;
}
.index_top button.el-button:hover,.index_top button.el-button:focus {
    background: #fff;
    border-color: var(--theme);
    color: var(--theme);
}

.index_top .menu-wrapper {
    order: 3;
}

.index_top .el-button.loginOut {
    background: var(--theme);
    border: none;
    border-radius: 4px;
    height: 30px;
    padding: 0px 10px;
    order: 5;
    margin: 0;
}
.bottom_view {
    width: 100%;
    background: rgba(0, 0, 0, 1);
    display: flex;
    justify-content: center;
    align-items: center;
    flex-direction: column;
    padding: 40px 0px;
    border-width: 0px 0px 0px;
    border-style: solid;
    border-color: rgb(254, 176, 67);
    font-size: 16px;
    color: rgb(51, 51, 51);
    line-height: 1.8;
}

.menu-wrapper {
    text-align: center;
    width: 100%;
}

.menu-wrapper .menu_view {
    display: flex;
    border-bottom: none;
    align-items: center;
    margin: 0 calc(50% - 694px) 0 calc(50% - 707px);
    background: rgba(243, 245, 247, 1);
    border-radius: 30px;
}

.menu-wrapper .first-item {
    color: rgba(102, 106, 111, 1)!important;
}

.menu-wrapper .el-sub-menu__title {
    color: inherit!important;
    border-bottom: none!important;
    height: 50px!important;
    line-height: 50px!important;
}

.menu-wrapper .el-sub-menu__title>* {
    color: inherit!important;
}

.menu-wrapper .first-item>i {
    color: inherit;
}

.menu-wrapper .first-item.is-active {
    color: #fff!important;
    border-bottom: none!important;
    background: var(--theme)!important;
    border-radius: 30px;
}
.menu-wrapper .first-item:hover {
    background: var(--theme)!important;
    color: #fff!important;
    line-height: 50px;
    height: 50px;
}
.menu-wrapper .el-menu-item.second-item.is-active {
    color: var(--theme)!important;
    background: #fff;
}

.menu-wrapper .second-item:hover {
    background: #fff!important;
    color: #333!important;
}

.menu_view>.first-item {
    line-height: 50px;
    height: 50px;
    border-radius: 30px;
    flex: 1;
    font-size: 18px;
}

li.el-menu-item.second-item:hover,li.el-menu-item.second-item.is-activ {
    color: var(--theme)!important;
}

.menu-wrapper .first-item .el-sub-menu__title {
    background: inherit!important;
    border-radius: 30px;
    font-size: 18px;
}

.menu-wrapper .first-item.is-active>.el-sub-menu__title {
    background: var(--theme)!important;
}

.rotation_view{
    width: 100%;
    margin: 0px auto;
    padding: 0 calc(50% - 700px);
}

.rotation_view .swiper-container{
    width: 100%;
    height: 100%;
    border:0px solid #fff;
    border-radius: 10px;
}
.rotation_view  .swiper .swiper-wrapper{
    transition-duration: 0ms;
    transform: translate3d(0px, 0px, 0px);
    height: 500px;
}
.rotation_view .swiper .swiper-wrapper .swiper-slide{
}
.rotation_view .swiper .swiper-wrapper .swiper-slide img{
    width: 100%;
    height: 100%;
    object-fit:cover;
}

.rotation_view .swiper .swiper-button-prev{
    margin-left:calc((100% - 1180px)/2);
    color:var(--theme);
}

.rotation_view .swiper .swiper-button-next{
    margin-right:calc((100% - 1180px)/2);
    color:var(--theme);
}

.bread_view {
    width:100%;
    background: none;
    color: rgb(51, 51, 51);
    text-align:center;
    font-size:20px;
    font-weight:600;
    border:0px solid #eee;
    padding:10px 0px 10px 0px;
    background: #fff;
}

.bread_view .el-breadcrumb{
    font-size: 16px;
    line-height: 1;
}

.bread_view .el-breadcrumb .el-breadcrumb__separator{
    margin: 0px 9px;
    font-weight: 500;
    color: #000;
}

.bread_view .el-breadcrumb .first_breadcrumb span a{
    color: #000;
    display: inline-block;
}

.bread_view .el-breadcrumb .second_breadcrumb .el-breadcrumb__inner{
    color: var(--theme);
    display: inline-block;
}


.bread_view {
    width: 1400px;
    margin: 0 calc(50vw - 700px);
    position: relative;
    left: calc(-50vw + 700px);
    height: 50px;
    align-items: center;
    display: flex;
    
    background: rgba(243, 245, 247, 1);
    padding: 0 50px;
    margin-top: 20px;
}

.back_view {
    padding: 10px 0;
}
.back_btn{
    background: var(--theme);
    border: none;
    color:#fff;
}
.back_btn:hover {
    background: var(--theme);
    color:#fff;
}

.bread_view .el-breadcrumb span {
    margin-right: 50px;
}

.chat_form {
    display: flex;
    
    margin: 20px auto;
    flex-wrap: wrap;
}

.chat_form .chat-content{
    margin: 0px 0px 30px;
    padding: 0px 0px 30px;
    height: 300px;
    overflow-y: scroll;
    border: 1px solid rgb(221, 221, 221);
    width: 100%;
}

.chat_form .chat-content .chat_time{
    width: 100%;
    text-align: center;
    font-size: 16px;
    color: rgb(153, 153, 153);
    padding: 6px 0px;
}

.chat_form .chat-content .right-content{
    float: right!important;
    margin: 0px 0px 10px;
    padding: 10px;
}
.chat_form .chat-content .right-content .text-content{
    background: var(--theme);
    color: #fff;
}
.chat_form .chat-content .right-content .chat_img{
    width: 150px;
    margin: 10px;
}

.chat_form .chat-content .left-content{
    float: left!important;
    margin: 0px 0px 10px;
    padding: 10px;
    width: auto;
}
.chat_form .chat-content .left-content .text-content{
    background: rgb(243 243 243);
    color: rgba(158, 158, 158, 1);
    margin: 0px 0px 0 10px;
}
.chat_form .chat-content .left-content .chat_img{
    width: 150px;
    margin: 10px;
}


.chat_form .input_box{
    display: flex;
    align-items: center;
    width: 100%;
    order: 2;
}

.chat_form .input_box .el-textarea .el-textarea__inner{
    width: 100%;
    min-height: 120px;
    padding: 12px;
    font-size: 14px;
    color: rgb(51, 51, 51);
    border: 1px solid rgb(221, 221, 221);
}

.chat_form .input_box .inp_add{
    width: 42px;
    cursor: pointer;
    margin: 0px 0px 0px 10px;
}


.chat_form .upload_box{
    display: flex;
    align-items: center;
    width: 100%;
    margin: 10px 0px;
}

.chat_form .upload_box .change_btn{
    margin: 0px 10px 0px 0px;
    padding: 0px 24px;
    width: auto;
    height: 32px;
    font-size: 16px;
    color: rgb(255, 255, 255);
    border-radius: 4px;
    border: 0px;
    background: rgba(118, 197, 191, 1);
    cursor: pointer;
}

.chat_form .upload_box .upload_btn{
    margin: 0px 10px 0px 0px;
    padding: 0px 24px;
    width: auto;
    height: 32px;
    font-size: 16px;
    color: rgb(255, 255, 255);
    border-radius: 4px;
    border: 0px;
    background: rgba(80, 191, 182, 1);
    cursor: pointer;
}

.chat_form .save_box{
    width: 100%;
    display: flex;
    justify-content: right;
    margin: 30px 0px 0px;
    order: 3;
}
.chat_form .save_box .save_btn{
    margin: 0px 10px 0px 0px;
    padding: 0px 24px;
    width: auto;
    height: 34px;
    font-size: 16px;
    color: rgb(255, 255, 255);
    border-radius: 4px;
    border: 0px;
    background: var(--theme);
    cursor: pointer;
    min-width: 100px;
}

</style>
<style lang="scss">
.home{--theme:#305d4c;background:#faf9f6}.home .index_top{height:auto;min-height:94px;padding:20px max(24px,calc((100% - 1200px)/2));background:#faf9f6;display:flex;flex-wrap:wrap;align-items:center;gap:12px 24px}.home .index_top .index_top_title{position:static;order:0;width:auto;flex-shrink:0;margin:0;padding:0;background:none;color:#294a3b;font-size:23px;letter-spacing:2px;line-height:1.5}.home .index_top .menu-wrapper{order:1;width:auto;flex:1;min-width:0}.home .menu-wrapper .menu_view{margin:0;background:transparent;border-radius:0;width:max-content;min-width:100%}.home .menu_view>.first-item,.home .menu-wrapper .first-item .el-sub-menu__title{font-size:14px;height:42px;line-height:42px;border-radius:3px;flex:initial;padding:0 14px}.home .menu-wrapper .first-item.is-active{border-radius:3px;background:#e8ede5!important;color:#305d4c!important}.home .menu-wrapper .first-item:hover{height:42px;line-height:42px}.home .index_top .index_top_right{position:static;order:2;width:auto;margin:0;display:flex;align-items:center;gap:16px}.home .index_top .chat{font-size:13px;color:#48614f}.home .index_top .login{height:36px;border-radius:3px}.home .contain_view{height:calc(100vh - 94px)}.home .bread_view{width:100%;max-width:1200px;left:0;margin:20px auto;padding:0 24px;background:#f0f1eb}.home .bread_view .el-breadcrumb span{margin-right:10px}.studio-footer{background:#233d32;color:#eef2e9;padding:48px max(24px,calc((100% - 1200px)/2));display:flex;flex-wrap:wrap;gap:24px;justify-content:space-between}.studio-footer strong{font-size:22px;letter-spacing:2px;font-weight:500}.studio-footer p{font-size:12px;color:#bccbbd;line-height:1.8}.studio-footer>div:nth-child(2){display:flex;gap:18px;align-items:center;font-size:12px}.studio-footer button{border:0;background:transparent;color:#e1e9dc;cursor:pointer;font:inherit;padding:10px 0}.studio-footer small{width:100%;border-top:1px solid #ffffff20;padding-top:20px;font-size:10px;color:#b5c3b3;letter-spacing:2px}@media(max-width:1000px){.home .index_top .menu-wrapper{order:3;width:100%;flex-basis:100%}.home .index_top{padding:14px 20px;justify-content:space-between}.home .contain_view{height:calc(100vh - 130px)}}@media(max-width:560px){.home .index_top .index_top_title{font-size:19px}.home .index_top .chat span{display:none}.home .menu_view>.first-item{padding:0 12px}.studio-footer>div:nth-child(2){flex-wrap:wrap}.home .bread_view{padding:0 16px}}
</style>

<style>.home{height:100vh;display:flex;flex-direction:column;overflow:hidden}.home .index_top{position:relative;flex:0 0 auto}.home .contain_view{padding:0;flex:1;min-height:0;height:auto}.home .index_top .index_top_right{padding:0;background:transparent}.home .index_top .index_top_title span{background:none;padding:0}</style>

<style>
.home .index_top .index_top_right{flex:0 0 auto;flex-wrap:nowrap;gap:20px;white-space:nowrap;min-width:max-content}
.home .index_top .index_top_right .chat{display:inline-flex;align-items:center;justify-content:center;gap:6px;flex:none;line-height:1;height:40px;white-space:nowrap}
.home .index_top .index_top_right .chat .iconfont{font-size:17px;line-height:1;margin:0}
.home .index_top .index_top_right .chat span{font-size:14px;line-height:20px}
.home .index_top .index_top_right .login.el-button{display:inline-flex;align-items:center;justify-content:center;flex:0 0 auto;width:auto;min-width:88px;height:40px;margin:0;padding:0 18px;border-radius:5px;line-height:1;white-space:nowrap;font-size:14px;overflow:visible}
.home .index_top .index_top_right .login.el-button .el-icon{flex:none;font-size:16px}
.home .index_top .index_top_right .login.el-button>span{display:inline-flex;align-items:center;line-height:20px;white-space:nowrap}
@media(max-width:560px){.home .index_top .index_top_right{gap:12px}.home .index_top .index_top_right .login.el-button{min-width:76px;padding:0 12px;height:38px}}
</style>

<style>
.home .index_top{border-bottom:1px solid #e8eade}.home .menu-wrapper .menu_view{border-bottom:0!important}.home .menu_view>.first-item>i{display:none}.home .menu-wrapper .first-item.is-active{background:transparent!important;color:#305d4c!important;border-bottom:2px solid #305d4c!important;border-radius:0}.home .menu-wrapper .first-item:hover{background:#edf0e7!important;color:#305d4c!important}.home .menu_view>.first-item{font-size:13px;letter-spacing:1px}.home .studio-detail{box-sizing:border-box;width:calc(100% - 80px);max-width:1200px;margin:0 auto;padding:38px 0 64px;background:transparent;border:0;box-shadow:none}.home .studio-detail .breadcrumb-wrapper{display:flex;align-items:center;justify-content:space-between;margin:0 0 32px;padding:0;background:none}.home .studio-detail .bread_view{width:auto;max-width:none;left:0;margin:0;padding:0;background:none;height:auto}.home .studio-detail .back_view{padding:0}.home .studio-detail .back_btn{background:transparent;color:#6b7b60;border:0}.home .studio-detail .detail_view{display:grid;grid-template-columns:minmax(0,1.2fr) minmax(0,1fr);gap:52px;width:100%;margin:0;padding:0;background:none;border:0;box-shadow:none;align-items:start}.studio-detail-gallery{display:grid;gap:18px;min-width:0}.studio-detail-gallery .el-image{width:100%;height:auto;aspect-ratio:4/3;background:#e9ebe4}.studio-detail-gallery .el-image:first-child{aspect-ratio:4/4.2}.home .studio-detail .info_view{width:100%;padding:0;margin:0;background:transparent;border:0;box-shadow:none;min-width:0}.home .studio-detail .title_view{margin:0 0 26px;padding:0;background:none;border:0}.home .studio-detail .detail_title{font-family:SimSun,serif;font-size:34px;line-height:1.5;font-weight:400;letter-spacing:2px;color:#294735;padding:0;margin:0;background:none;text-align:left}.home .studio-detail .info_item{display:block;width:100%;padding:17px 0;margin:0;border:0;border-bottom:1px solid #e3e7dc;background:transparent;min-height:0}.home .studio-detail .info_label{display:block;width:auto;padding:0;margin:0 0 7px;color:#939c87;font-size:11px;background:none;line-height:1.6;text-align:left}.home .studio-detail .info_text{display:block;width:auto;padding:0;margin:0;color:#53644c;font-size:14px;line-height:1.95;overflow-wrap:anywhere;background:none;text-align:left}.home .studio-detail .btn_view{display:flex;gap:10px;flex-wrap:wrap;margin-top:26px;padding:0;width:100%;background:none}.home .studio-detail .btn_view .el-button{margin:0;height:42px;border-radius:3px}.home .studio-detail .cross_btn{background:#305d4c;color:#fff;border-color:#305d4c;padding:0 24px}.home .studio-detail .tabs_view{width:100%;margin:56px 0 0;padding:0;background:none;border:0;box-shadow:none}.home .studio-detail .el-tabs__header{background:none;margin:0 0 26px;padding:0;border:0}.home .studio-detail .el-tabs__content{padding:0;background:none;overflow:visible;font-size:14px;line-height:1.95;color:#5e6e55}.home .studio-detail .el-tabs__content img{max-width:100%;height:auto}.home .studio-detail .my_comment_view{padding:20px 0;background:none;width:100%;border:0}.home .studio-detail .comment_list{padding:0;background:none}.home .studio-detail .comment{padding:24px 0;border:0;border-bottom:1px solid #e2e6db;background:none;box-shadow:none}.home .studio-community{box-sizing:border-box;max-width:1080px;width:calc(100% - 80px);margin:0 auto;padding:64px 0;background:transparent;border:0;box-shadow:none}.community-heading>span{font-size:10px;letter-spacing:3px;color:#939b87}.community-heading h1{font-family:SimSun,serif;font-size:44px;font-weight:400;letter-spacing:4px;color:#294735;margin:20px 0}.community-heading p{font-size:13px;line-height:1.9;color:#7e8875}.home .studio-community>.breadcrumb-wrapper{display:none}.home .studio-community .list_search{display:flex;align-items:center;justify-content:space-between;gap:18px;flex-wrap:wrap;width:100%;margin:36px 0 0;padding:20px 0;background:none;border:0;border-top:1px solid #e0e4d8;border-bottom:1px solid #e0e4d8;border-radius:0}.home .studio-community .search_view{display:flex;align-items:center;gap:12px;margin:0;padding:0;width:auto;background:none}.home .studio-community .search_label{font-size:12px;color:#7e8875;padding:0;width:auto;background:none}.home .studio-community .search_box{width:260px;padding:0}.home .studio-community .search_inp{width:100%;height:auto;padding:0;border:0;background:none}.home .studio-community .search_btn_view{margin:0;padding:0;display:flex;gap:10px}.home .studio-community .search_btn_view .el-button{margin:0;border-radius:3px}.home .studio-community .forum_list{width:100%;margin:0;padding:0;background:none;border:0;box-shadow:none}.home .studio-community .forum_item{display:flex;align-items:center;justify-content:space-between;gap:24px;width:100%;padding:28px 0;margin:0;background:none;border:0;border-bottom:1px solid #e3e7dc;border-radius:0;box-shadow:none;min-height:100px;cursor:pointer}.home .studio-community .forum_item:hover{background:#f0f2eb}.home .studio-community .forum_item_left{display:flex;flex-direction:column;align-items:flex-start;gap:12px;min-width:0;padding:0;margin:0;width:auto}.home .studio-community .forum_item_title{font-size:19px;color:#344e39;line-height:1.5;white-space:normal}.home .studio-community .forum_item_name,.home .studio-community .forum_time{font-size:11px;color:#919b84;background:none;line-height:1.8;padding:0;flex-shrink:0}.community-thread{margin-top:44px;border-top:1px solid #dbe2d3;padding-top:24px}.home .community-thread .forum_detail{width:100%;padding:24px 0;margin:0;background:none;border:0;box-shadow:none}.home .community-thread .forum_title{font-size:30px;color:#294735;background:none;padding:0;line-height:1.6}.home .community-thread .forum_info{font-size:12px;color:#8b947f;display:flex;flex-wrap:wrap;gap:20px;margin:18px 0}.home .community-thread .forum_content{font-size:14px;line-height:2;color:#53644c;padding:0}.community-thread img{max-width:100%;height:auto}.home .community-thread .forum_comment_box{padding:0;margin-top:24px;border:0;box-shadow:none;background:none}.home .community-thread .forum_comment{padding:24px 0;border-bottom:1px solid #e2e7da;background:none;box-shadow:none}.home .community-thread .forum_comment_second_list{background:#f0f2ea;padding:16px;margin-top:16px;border:0}.home .studio-community .el-pagination,.home .studio-detail .el-pagination{width:100%;max-width:100%;margin:30px 0;padding:0;display:flex;justify-content:flex-start;flex-wrap:wrap;gap:5px;background:none}.home{--el-color-primary:#305d4c}.home .el-button--primary{--el-button-bg-color:#305d4c;--el-button-border-color:#305d4c;--el-button-hover-bg-color:#244b3b;--el-button-hover-border-color:#244b3b}.home .el-dialog{max-width:calc(100vw - 24px);border-radius:10px}.home .studio-community .forum_item_btn_box{display:flex;gap:8px;flex-shrink:0}
@media(max-width:900px){.home .studio-detail .detail_view{grid-template-columns:1fr;gap:32px}.studio-detail-gallery{grid-template-columns:repeat(2,minmax(0,1fr))}.studio-detail-gallery .el-image:first-child{grid-column:1/-1;aspect-ratio:4/3}.home .studio-detail .detail_title{font-size:30px}}@media(max-width:560px){.home .studio-detail,.home .studio-community{width:calc(100% - 40px);padding:32px 0 44px}.community-heading h1{font-size:32px}.home .studio-community .forum_item{align-items:flex-start;flex-direction:column;gap:14px}.home .studio-community .search_box{width:100%;min-width:0}.home .studio-community .search_view{width:100%}.home .studio-community .forum_item_title{font-size:17px}.studio-detail-gallery{gap:10px}.home .studio-detail .detail_title{font-size:27px}.home .studio-detail .info_text{font-size:13px}.home .studio-detail .tabs_view{margin-top:36px}}
</style>

<style>
.home .studio-detail-gallery .el-image__inner{width:100%!important;height:100%!important;max-width:100%;object-fit:cover}.home .studio-detail-gallery .el-image__wrapper{width:100%;height:100%}.home .studio-detail .info_view{box-shadow:none!important;border:0!important}.home .studio-community .forum_list{display:block}.home .studio-community .forum_item{position:static;float:none;box-sizing:border-box}.home .studio-community .forum_item_left{position:static}.home .studio-community .forum_time{position:static;background:transparent;color:#919b84}.home .studio-community .list_search{position:static;box-sizing:border-box}.home .studio-community .search_btn_view{position:static}.home .studio-detail .info_item{box-sizing:border-box}.home .studio-community .forum_item_title{font-weight:400}.home .studio-community .forum_comment_user{display:flex;gap:10px;align-items:center}.home .studio-community .forum_comment_user_avatar,.home .studio-community .forum_comment_second_user_avatar{width:32px;height:32px;border-radius:50%;object-fit:cover}.home .studio-community .forum_comment_content{font-size:14px;line-height:1.9;margin:16px 0}.home .studio-community .forum_comment_reply_view{display:flex;gap:14px;font-size:12px;color:#72836a}.home .studio-detail .comment_user_img img{width:36px;height:36px;border-radius:50%;object-fit:cover}.home .studio-detail .comment_user,.home .studio-detail .comment_top{display:flex;gap:12px;align-items:center}.home .studio-detail .comment_top{justify-content:space-between}.home .studio-detail .comment_time{font-size:11px;color:#939b87}.home .studio-detail .comment_bottom{padding-top:16px}.home .studio-detail .comment_btn{display:flex;gap:10px;margin:16px 0}.home .studio-detail .my_comment_form{width:100%;padding:0;background:none}.home .studio-detail .list_editor{width:100%;max-width:100%}
</style>

<style>.home .studio-community .search_view .search_label{flex:none!important;width:auto!important;white-space:nowrap}.home .studio-community .search_view .search_box{flex:1;min-width:0}.home .studio-community .search_view .search_inp{border:0!important;padding:0!important}.home .studio-community .search_view .el-input__wrapper{background:transparent;box-shadow:none;border-bottom:1px solid #dce2d3;border-radius:0}.home .studio-community .forum_item .forum_item_title{font-weight:400!important}.home .studio-community .community-thread{scroll-margin-top:20px}</style>

<style>.home .index_top .index_top_title{gap:10px}.home .huahai-brand-mark{width:42px;height:42px;flex:none;object-fit:contain}@media(max-width:560px){.home .huahai-brand-mark{width:32px;height:32px}.home .index_top .index_top_title{gap:7px;font-size:17px}}</style>
