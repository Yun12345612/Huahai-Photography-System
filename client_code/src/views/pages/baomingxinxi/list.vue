<template>
	<div class="list-page" :style='{}'>
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
		<el-form :inline="true" :model="searchQuery" class="list_search" @submit.native.prevent>
			<div class="search_view">
				<div class="search_label">
					活动名称：
				</div>
				<div class="search_box">
					<el-input class="search_inp" v-model="searchQuery.huodongmingcheng" placeholder="活动名称"
						clearable>
					</el-input>
				</div>
			</div>
			<div class="search_btn_view">
				<el-button class="search_btn" type="primary" @click="searchClick">搜索</el-button>
				<el-button class="add_btn" type="success" v-if="btnAuth('baomingxinxi','新增')" @click="addClick">新增</el-button>
			</div>
		</el-form>
		<div class="page_list">
			<div class="data_box">
                <div class="table_view">
					<el-table v-loading="listLoading" class="data_table" :data="list" border :row-style="{'cursor':'pointer'}"
						@row-click="tableDetailClick" :stripe='true'>
						<el-table-column :resizable='true' align="left" header-align="left" type="selection" width="55" />
						<el-table-column min-width="120" label="序号" width="120" :resizable='true' align="left" header-align="left">
							<template #default="scope">{{ (listQuery.page-1)*listQuery.limit+scope.$index + 1}}</template>
						</el-table-column>
						<el-table-column min-width="120" label="活动名称" :resizable='true' align="left" header-align="left">
							<template #default="scope">
								{{scope.row.huodongmingcheng}}
							</template>
						</el-table-column>
						<el-table-column min-width="120" label="活动图片" :resizable='true' align="left" header-align="left">
							<template #default="scope">
								<div v-if="scope.row.huodongtupian">
									<el-image v-if="scope.row.huodongtupian.substring(0,4)=='http'" preview-teleported
										:preview-src-list="[scope.row.huodongtupian.split(',')[0]]"
										:src="scope.row.huodongtupian.split(',')[0]" style="width:100px;height:100px" @click.stop></el-image>
									<el-image v-else preview-teleported
										:preview-src-list="[$config.url+scope.row.huodongtupian.split(',')[0]]"
										:src="$config.url+scope.row.huodongtupian.split(',')[0]" style="width:100px;height:100px" @click.stop>
									</el-image>
								</div>
								<div v-else>无图片</div>
							</template>
						</el-table-column>
						<el-table-column min-width="120" label="开始时间" :resizable='true' align="left" header-align="left">
							<template #default="scope">
								{{scope.row.kaishishijian}}
							</template>
						</el-table-column>
						<el-table-column min-width="120" label="结束时间" :resizable='true' align="left" header-align="left">
							<template #default="scope">
								{{scope.row.jieshushijian}}
							</template>
						</el-table-column>
						<el-table-column min-width="120" label="活动地点" :resizable='true' align="left" header-align="left">
							<template #default="scope">
								{{scope.row.huodongdidian}}
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
		<el-dialog v-model="preViewVisible" :title="'查看大图'" width="40%" destroy-on-close>
            <div style="text-align:center">
                <img :src="preViewUrl" style="max-width: 100%;" alt="">
            </div>
		</el-dialog>
	</div>
</template>

<script setup>
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
    const store = useStore()
    const user = computed(()=>store.getters['user/session'])
	const context = getCurrentInstance()?.appContext.config.globalProperties;
	const router = useRouter()
	const route = useRoute()
	//基础信息
	const tableName = 'baomingxinxi'
	const formName = '报名信息'
	//基础信息
	const breadList = ref([{
		name: formName
	}])
	const list = ref([])
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
		router.push('/index/baomingxinxiAdd')
	}
	//判断是否从个人中心跳转
	const centerType = ref(false)
	//返回
	const backClick = () => {
		router.push(`/index/${context?.$toolUtil.storageGet('frontSessionTable')}Center`)
	}
	//搜索
	const searchQuery = ref({})
	//下拉列表
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
		if(searchQuery.value.huodongmingcheng&&searchQuery.value.huodongmingcheng!=''){
			params.huodongmingcheng = '%' + searchQuery.value.huodongmingcheng + '%'
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
	const init = async() => {
		if(route.query.centerType){
			centerType.value = true
		}
        if(context.$toolUtil.storageGet('frontToken') && !user.value.id){
            await store.dispatch("user/getSession")
        }
		getList()
	}
	init()
</script>
