<template>
	<div class="detail-page studio-detail" :style='{}'>
        <div class="breadcrumb-wrapper" style="width: 100%;">
            <div class="bread_view">
                <el-breadcrumb separator="" class="breadcrumb">
                    <el-breadcrumb-item class="first_breadcrumb" :to="{ path: '/' }">首页</el-breadcrumb-item>
                    <el-breadcrumb-item class="second_breadcrumb" v-for="(item,index) in breadList" :key="index">{{item.name}}</el-breadcrumb-item>
                </el-breadcrumb>
            </div>
            <div class="back_view">
                <el-button class="back_btn" @click="backClick" type="primary">返回</el-button>
            </div>
        </div>
		<div class="detail_view">
            <div class="studio-detail-gallery"><el-image v-for="(photo,index) in bannerList" :key="index" :src="/^https?:/.test(photo) ? photo : $config.url+photo" :preview-src-list="bannerList.map(value => /^https?:/.test(value) ? value : $config.url+value)" :initial-index="index" fit="cover" loading="lazy" /></div>

			<div class="info_view">
				<div class="title_view">
					<div class="detail_title">
						<span>{{detail.zuopinmingcheng}}</span>
					</div>
				</div>
				
				<div class="info_item">
					<div class="info_label">摄影姓名</div>
					<div  class="info_text" >{{detail.sheyingxingming}}</div>
				</div>
				<div class="info_item">
					<div class="info_label">作品简介</div>
					<div  class="info_text" >{{detail.zuopinjianjie}}</div>
				</div>
				<div class="info_item">
					<div class="info_label">作品概述</div>
					<div  class="info_text" >{{detail.zuopingaishu}}</div>
				</div>
				<div class="info_item">
					<div class="info_label">创作背景</div>
					<div  class="info_text" >{{detail.chuangzuobeijing}}</div>
				</div>
				<div class="info_item">
					<div class="info_label">发布时间</div>
					<div  class="info_text" >{{detail.fabushijian}}</div>
				</div>
				<div class="btn_view">
					<el-button class="edit_btn" v-if="centerType&&btnAuth('sheyingzuopin','修改')" type="primary" @click="editClick">修改</el-button>
					<el-button class="del_btn" v-if="centerType&&btnAuth('sheyingzuopin','删除')" type="danger" @click="delClick">删除</el-button>
				</div>
			</div>
		</div>
	</div>
</template>
<script setup>
	import axios from 'axios'
	import moment from 'moment'
	import {
		ref,
		getCurrentInstance,
		watch,
		onUnmounted,
		onMounted,
		nextTick,
		computed,
        inject
	} from 'vue';
	import {
		ElMessageBox
	} from 'element-plus'
	import {
		useRoute,
		useRouter
	} from 'vue-router';
	import {
		useStore
	} from 'vuex';
	const store = useStore()
	const user = computed(()=>store.getters['user/session'])
	const userAvatar = computed(()=>store.getters['user/avatar'])
	const context = getCurrentInstance()?.appContext.config.globalProperties;
	const route = useRoute()
	const router = useRouter()
	//基础信息
	const tableName = 'sheyingzuopin'
	const formName = '摄影作品'
	//基础信息
	const breadList = ref([{
		name: formName
	}])
	//权限验证
	const btnAuth = (e,a)=>{
		if(centerType.value){
			return context?.$toolUtil.isBackAuth(e,a)
		}else{
			return context?.$toolUtil.isAuth(e,a)
		}
	}
	//查看权限验证
	const btnFrontAuth = (e,a)=>{
		if(centerType.value){
			return context?.$toolUtil.isBackAuth(e,a)
		}else{
			return context?.$toolUtil.isFrontAuth(e,a)
		}
	}
	// 返回
	const backClick = () =>{
		history.back()
	}
	// 轮播图
	const bannerList = ref([])
	// 详情
	const title = ref('')
	const detail = ref({})
    const activeName = ref('false')
	const getDetail = () => {
		context?.$http({
			url: `${tableName}/detail/${route.query.id}`,
			method: 'get'
		}).then(res => {
            bannerList.value = res.data.data.zuopintupian?res.data.data.zuopintupian.split(','):[]
            title.value = res.data.data.zuopinmingcheng
			detail.value = res.data.data
		})
	}
	// 下载文件
	const downClick = (file) => {
		if(!file){
			context?.$toolUtil.message('文件不存在','error')
		}
		let arr = file.replace(new RegExp('file/', "g"), "")
		axios.get((location.href.split(context?.$config.name).length>1 ? location.href.split(context?.$config.name)[0] :'') + context?.$config.name + '/file/download?fileName=' + arr, {
			headers: {
				token: context?.$toolUtil.storageGet('frontToken')
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
	// 判断是否从个人中心跳转
	const centerType = ref(false)
	const init = () => {
		if(route.query.centerType){
			centerType.value = true
		}
		getDetail()
	}
	//修改
	const editClick = () => {
		router.push(`/index/${tableName}Add?id=${detail.value.id}&&type=edit`)
	}
	//删除
	const delClick = () => {
		ElMessageBox.confirm(`是否删除此${formName}？`, '提示', {
			confirmButtonText: '是',
			cancelButtonText: '否',
			type: 'warning',
		}).then(()=>{
			context?.$http({
				url: `${tableName}/delete`,
				method: 'post',
				data: [detail.value.id]
			}).then(res=>{
				context?.$toolUtil.message('删除成功','success',()=>{
					history.back()
				})
			})

		}).catch(_ => {})
	}
	onMounted(()=>{
		init()
	})
</script>

<style scoped>
.studio-detail .detail_view .info_view{width:100%!important;min-width:0!important;padding:0!important;background:none!important;border:0!important;box-shadow:none!important}.studio-detail .detail_view .info_view .title_view{padding:0!important;margin:0 0 24px!important;background:none!important}.studio-detail .detail_view .info_view .detail_title{font-size:32px!important;line-height:1.6!important;font-weight:400!important;color:#294735!important;padding:0!important;text-align:left!important}.studio-detail .detail_view .info_view .info_item{display:block!important;width:100%!important;padding:16px 0!important;margin:0!important;background:none!important;border:0!important;border-bottom:1px solid #e2e6db!important}.studio-detail .detail_view .info_view .info_label{display:block!important;width:auto!important;padding:0!important;margin:0 0 7px!important;font-size:11px!important;color:#8b967f!important;text-align:left!important;line-height:1.6!important}.studio-detail .detail_view .info_view .info_text{display:block!important;width:auto!important;padding:0!important;margin:0!important;font-size:14px!important;color:#53644c!important;line-height:1.95!important;overflow-wrap:anywhere!important}.studio-detail .detail_view .btn_view .cross_btn{background:#305d4c!important;border-color:#305d4c!important;color:white!important}.studio-detail :deep(.el-image__inner){width:100%!important;height:100%!important;object-fit:cover!important}@media(max-width:560px){.studio-detail .detail_view .info_view .detail_title{font-size:27px!important}}
</style>
