<template>
    <StudioCollection v-if="!$route.query.centerType" type="sheyingshixinxi" />
	<div v-else class="list-page" :style='{}'>
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
					摄影账号：
				</div>
				<div class="search_box">
					<el-input class="search_inp" v-model="searchQuery.sheyingzhanghao" placeholder="摄影账号"
						clearable>
					</el-input>
				</div>
			</div>

    <div class="search_view">
        <div class="search_label">
            分类：
        </div>
        <div class="search_box">
            <el-select
                class="search_sel"
                clearable
                v-model="searchQuery.fenlei"
                placeholder="分类"
            >
                <el-option v-for="item in fenleiLists" :label="item" :value="item"></el-option>
            </el-select>
        </div>
    </div>
			<div class="search_btn_view">
				<el-button class="search_btn" type="primary" @click="searchClick">搜索</el-button>
				<el-button class="add_btn" type="success" v-if="btnAuth('sheyingshixinxi','新增')" @click="addClick">新增</el-button>
			</div>
		</el-form>
		<div class="category_view">
			<div class="category" :class="categoryIndex==-1?'categoryActive':''" @click="categoryClick(-1)">全部
			</div>
			<div class="category" :class="categoryIndex==index?'categoryActive':''"
				v-for="(item,index) in categoryList" :key="index" @click="categoryClick(index)">{{item}}</div>
		</div>
		<div class="page_list">
			<div class="data_box">
				<div class="data_view">
					<div class="data_item" v-for="(item,index) in list" :key="index" @click.stop="detailClick(item.id)" >
						<div class="data_img_box" v-if="item.sheyingshizhaopian && item.sheyingshizhaopian.substr(0,4)=='http'" @click.stop="preViewClick(item.sheyingshizhaopian)">
							<el-image class="data_img" :src="item.sheyingshizhaopian" fit="cover"></el-image>
						</div>
						<div class="data_img_box" v-else @click.stop="preViewClick($config.url+item.sheyingshizhaopian.split(',')[0])">
							<el-image class="data_img" :src="item.sheyingshizhaopian?$config.url + item.sheyingshizhaopian.split(',')[0]:''"
								fit="cover"></el-image>
						</div>
						<div class="data_content">
							<div class="data_title"><span>{{item.sheyingzhanghao}}</span></div>
							<div class="data_title"><span>{{item.jianjie}}</span></div>
							<div class="data_price" v-if="item.price"><span>￥</span><span>{{item.price}}</span></div>
							<div class="data_operate_box">
								<div class="data_like" v-if="item.thumbsupNumber">
									<span class="iconfont icon-thumb-up-line1"></span>
									<div class="like_num">{{item.thumbsupNumber}}</div>
								</div>
								<div class="data_collect" v-if="item.storeupNumber">
									<el-icon><StarFilled /></el-icon>
									<div class="collect_num">{{item.storeupNumber}}</div>
								</div>
								<div class="data_clickNum" v-if="item.clickNumber">
									<el-icon><View /></el-icon>
									<div class="clickNum_num">{{item.clickNumber}}</div>
								</div>
							</div>
						</div>
					</div>
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
 import StudioCollection from "@/components/StudioCollection.vue"
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
	const tableName = 'sheyingshixinxi'
	const formName = '摄影师信息'
	//基础信息
	const breadList = ref([{
		name: formName
	}])
	const list = ref([])
	const listQuery = ref({
		page: 1,
		limit: Number(8)
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
		router.push('/index/sheyingshixinxiAdd')
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
	const fenleiLists = ref([])
	const getfenleiLists = () => {
		context?.$http({
			url: 'option/fenlei/fenlei',
			method:'get'
		}).then(res=>{
			fenleiLists.value = res.data.data
		})
	}
	getfenleiLists()
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
		if (categoryIndex.value != -1) {
			params.fenlei = categoryList.value[categoryIndex.value]
		}
		if(searchQuery.value.sheyingzhanghao&&searchQuery.value.sheyingzhanghao!=''){
			params.sheyingzhanghao = '%' + searchQuery.value.sheyingzhanghao + '%'
		}
		if(searchQuery.value.fenlei&&searchQuery.value.fenlei!=''){
			params.fenlei = searchQuery.value.fenlei
		}
		if(!centerType.value){
			params['sfsh'] = '是';
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
	//分类
	const categoryList = ref([])
	const categoryIndex = ref(-1)
	const getCategoryList = () => {
		context?.$http({
			url: 'option/fenlei/fenlei',
			method: 'get'
		}).then(res => {
			categoryList.value = res.data.data
		})
	}
	const categoryClick = (index) => {
		listQuery.value.page = 1
		categoryIndex.value = index
		getList()
	}
	const detailClick = (id) => {
		router.push(`${tableName}Detail?id=` + id + (centerType.value?'&&centerType=1':''))
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
		getCategoryList()
        if(context.$toolUtil.storageGet('frontToken') && !user.value.id){
            await store.dispatch("user/getSession")
        }
		getList()
	}
	init()
</script>
<style lang="scss" scoped>
	// 返回盒子
	.back_view {
		// 返回按钮
		.back_btn {
		}
		// 返回按钮-悬浮
		.back_btn:hover {
		}
	}
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
	// 分类盒子
	.category_view {
		// 分类item
		.category {
		}
		// item-悬浮
		.category:hover {
		}
		// item-选中
		.categoryActive {
		}
	}

	//搜索
	.list_search {
		.search_view {
			.search_label {
			}
			.search_box {
				// 输入框
				:deep(.search_inp) {
					.is-focus {
						box-shadow: none !important;
					}
				}
				// 下拉框
				:deep(.search_sel) {
					//去掉默认样式
					.select-trigger{
						height: 100%;
						.el-input{
							height: 100%;
							.is-focus {
								box-shadow: none !important;
							}
						}
					}
				}
			}
		}
		.search_btn_view {
			// 搜索按钮
			.search_btn {
			}
			// 搜索按钮-悬浮
			.search_btn:hover {
			}
			// 新增按钮
			.add_btn {
			}
			// 新增按钮-悬浮
			.add_btn:hover {
			}
		}
	}

	// 数据盒子
	.page_list {
		//列表
		.data_box {
			.data_view {
				.data_item {
					// 图片盒子
					.data_img_box {
						// 图片
						.data_img {
						}
					}
					// 内容盒子
					.data_content {
						// 价格
						.data_price {
						}
						// 标题
						.data_title {
						}
						// 底部栏
						.data_operate_box {
							// 点赞数
							.data_like {
								.like_icon {
								}
								.like_num {
								}
							}
							// 收藏数
							.data_collect {
								.el-icon {
								}
								.collect_num {
								}
							}
							// 点击数
							.data_clickNum {
								.el-icon {
								}
								.clickNum_num {
								}
							}
						}
					}
				}
			}
		}
	}

	// 分页器
	.el-pagination {
		// 总页码
		:deep(.el-pagination__total) {
		}
		// 上一页
		:deep(.btn-prev) {
		}
		// 下一页
		:deep(.btn-next) {
		}
		// 上一页禁用
		:deep(.btn-prev:disabled) {
		}
		// 下一页禁用
		:deep(.btn-next:disabled) {
		}
		// 页码
		:deep(.el-pager) {
			// 数字
			.number {
			}
			// 数字悬浮
			.number:hover {
			}
			// 选中
			.number.is-active {
			}
		}
		// sizes
		:deep(.el-pagination__sizes) {
			.el-select {
				//去掉默认样式
				.select-trigger{
					height: 100%;
					.el-input{
						height: 100%;
						.is-focus {
							box-shadow: none !important;
						}
					}
				}
			}
		}
		// 跳页
		:deep(.el-pagination__jump) {
			// 输入框
			.el-input {
				.is-focus {
					box-shadow: none !important;
				}
			}
		}
	}
	
	// 热门信息盒子
	.hot_view {
		// 标题
		.hot_title {
		}

		.hot_list {
			// item
			.hot {
				//图片盒子
				.hot_img_view {
					// 图片
					.hot_img {
					}
				}
				// 内容盒子
				.hot_content {
					// 名称
					.hot_text {
					}
				}
			}
		}
	}
</style>