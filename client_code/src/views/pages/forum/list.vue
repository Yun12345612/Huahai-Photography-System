<template>
	<div class="forum_view studio-community" :style='{}'>
        <header class="community-heading"><span>THE CREATIVE COMMUNITY</span><h1>聊聊，镜头之外</h1><p>分享拍摄故事，交换灵感，让热爱摄影的人在这里相遇。</p></header>
        <div class="breadcrumb-wrapper" style="width: 100%;">
            <div class="bread_view">
                <el-breadcrumb separator="" class="breadcrumb">
                    <template v-if="centerType">
                        <el-breadcrumb-item class="second_breadcrumb" :to="{ path: `/index/${sessionTable}Center` }">个人中心</el-breadcrumb-item>
                        <el-breadcrumb-item class="second_breadcrumb">我的发布</el-breadcrumb-item>
                    </template>
                    <template v-else>
                        <el-breadcrumb-item class="first_breadcrumb" :to="{ path: '/' }">首页</el-breadcrumb-item>
                        <el-breadcrumb-item class="second_breadcrumb">{{formName}}</el-breadcrumb-item>
                    </template>
                </el-breadcrumb>
            </div>
            <div class="back_view" v-if="centerType">
                <el-button class="back_btn" @click="backClick" type="primary">返回</el-button>
            </div>
        </div>
		<el-form :model="searchQuery" class="list_search" @submit.native.prevent>
			<div class="search_view">
				<div class="search_label">
					标题：
				</div>
				<div class="search_box">
					<el-input class="search_inp" v-model="searchQuery.title" placeholder="标题" size="small" clearable>
					</el-input>
				</div>
			</div>
			<div class="search_btn_view">
				<el-button class="search_btn" type="primary" @click="searchClick">搜索</el-button>
				<el-button class="add_btn" @click="addClick" v-if="!myType&&btnAuth('forum','新增')" type="success">发布新帖</el-button>
			</div>
		</el-form>
		<div class="forum_list">
			<div class="forum_item" v-for="(item,index) in list" :key="index" @mouseenter="forumEnter(index)"
				@mouseleave="forumLeave" @click.stop="detailClick(item.id)">
				<div class="forum_item_left">
					<span class="forum_item_title">{{item.title}}</span>
					<span class="forum_item_name">
                        (发布人：{{item.username}})
                    </span>
				</div>
				<div class="forum_item_btn_box" v-if="item.userid==userid&&forumShowIndex==index&&(btnAuth('forum','修改')||btnAuth('forum','删除'))">
					<el-button class="forum_edit_btn" v-if="btnAuth('forum','修改')" type="primary" @click.stop="editClick(item.id)">修改</el-button>
					<el-button class="forum_del_btn" v-if="btnAuth('forum','删除')" type="danger" @click.stop="delClick(item.id)">删除</el-button>
				</div>
				<div class="forum_time" v-else>{{item.addtime}}</div>
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
				@current-change="currentChange" />
		</div>
		
		<el-dialog v-model="formVisible" :title="formTitle" width="70%" class="edit_view" destroy-on-close>
			<el-form class="add_form" :model="form" label-width="120px" :rules="rules" ref="formRef">
				<el-row>
					<el-col :span="12">
						<el-form-item label="标题" prop="title">
							<el-input class="list_inp" v-model="form.title" placeholder="请输入标题"></el-input>
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="类型">
							<el-radio-group class="list_radio" v-model="form.isdone">
								<el-radio label="开放">公开</el-radio>
								<el-radio label="关闭">私人</el-radio>
							</el-radio-group>
						</el-form-item>
					</el-col>
					<el-col :span="24">
						<el-form-item label="内容" prop="content">
							<editor :value="form.content" placeholder="请输入内容"
								class="list_editor" @change="contentChange"></editor>
						</el-form-item>
					</el-col>
				</el-row>
			</el-form>
			<template #footer>
				<span class="formModel_btn_box">
					<el-button class="formModel_cancel" @click="formVisible=false">关闭</el-button>
					<el-button class="formModel_confirm" type="primary" @click="save">{{form.id?'修改保存':'发布'}}</el-button>
				</span>
			</template>
		</el-dialog>
		<section v-if="detailVisible" class="community-thread"><el-button text @click="detailVisible=false">← 收起这篇故事</el-button>
			<div class="forum_detail">
				<div class="forum_title">
					<span>{{detailForm.title}}</span>
				</div>
				<div class="forum_info">
					<span class="forum_name">
                        发布人：{{detailForm.username}}
                    </span>
					<span class="forum_time">发布时间：{{detailForm.addtime}}</span>
				</div>
				<el-divider />
				<div class="forum_content" v-html="detailForm.content"></div>
			</div>
			<section class="forum_comment_box">
				<el-divider class="comment_divider" content-position="left">评论列表</el-divider>
				<div class="comment_add_box">
					<el-button class="comment_add" @click="commentClick" type="success">发表评论</el-button>
				</div>
				<div class="forum_comment_list">
					<div class="forum_comment" v-for="(item,index) in detailForm.childs" :key="index"
						@mouseenter="commentEnter(index)" @mouseleave="commentLeave">
						<div class="forum_comment_user">
							<img v-if="item.avatarurl" class="forum_comment_user_avatar"
								:src="$config.url + item.avatarurl.split(',')[0]" alt="">
							<img v-else class="forum_comment_user_avatar" src="@/assets/avatar.png" alt="">
							<span class="forum_comment_username">{{item.username}}</span>
						</div>
						<div class="forum_comment_content" v-html="item.content"></div>
						<div class="forum_comment_reply_view">
							<div class="forum_comment_del" v-if="commentShowIndex==index&&item.userid==userid"
								@click="commentDelClick(item.id)">删除</div>
							<div class="forum_comment_reply" v-if="commentShowIndex==index"
								@click="commentReplyClick(item.id)">回复</div>
						</div>
						<div class="forum_comment_second_list" v-if="item.childs">
							<el-divider class="forum_comment_second_divider" content-position="center">回复</el-divider>
							<div class="forum_comment_second" v-for="(items,indexs) in item.childs" :key="indexs"
								@mouseenter="commentEnter1(indexs)" @mouseleave="commentLeave1">
								<div class="forum_comment_second_user">
									<img v-if="items.avatarurl" class="forum_comment_second_user_avatar"
										:src="$config.url + items.avatarurl.split(',')[0]" alt="">
									<img v-else class="forum_comment_second_user_avatar" src="@/assets/avatar.png" alt="">
									<span class="forum_comment_second_username">{{items.username}}</span>
								</div>
								<div class="forum_comment_second_content" v-html="items.content"></div>
								<div class="forum_comment_second_reply_view">
									<div class="forum_comment_second_del" 
										v-if="commentShowIndex==index&&items.userid==userid&&commentShowIndex1==indexs"
										@click="commentDelClick(items.id)">删除</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</section>
		</section>
		<el-dialog v-model="commentVisible" :title="'发表评论'" width="50%" destroy-on-close>
			<el-form class="add_form" :model="commentForm" label-width="120px">
				<el-row>
					<el-col :span="24">
						<el-form-item prop="content" label="内容">
							<editor :value="commentForm.content" placeholder="请输入评论"
								class="list_editor" @change="commentChange"></editor>
						</el-form-item>
					</el-col>
				</el-row>
				
			</el-form>
			<template #footer>
				<span class="formModel_btn_box">
					<el-button class="formModel_cancel" @click="commentVisible=false">关闭</el-button>
					<el-button class="formModel_confirm" type="primary" @click="commentSave">发表</el-button>
				</span>
			</template>
		</el-dialog>
	</div>
</template>

<script setup>
	import {
		ref,
		nextTick,
		getCurrentInstance
	} from 'vue';
	import {
		ElMessageBox
	} from 'element-plus'
	import {
		useRoute,
		useRouter
	} from 'vue-router'
	const context = getCurrentInstance()?.appContext.config.globalProperties;
	//基础信息
	const tableName = 'forum'
	const formName = '互动社交'
	const router = useRouter()
	const route = useRoute()
    const sessionTable = localStorage.getItem('frontSessionTable')
	//权限验证
    const btnAuth = (e, a) => {
        if(context?.$toolUtil.isAuth(e, a)){
            return true
        }
        if(centerType.value){
            return context?.$toolUtil.isBackAuth(e,a)
        }
    }
	const list = ref([])
	const listLoading = ref(false)

	const listQuery = ref({
		page: 1,
		limit: 20,
		isdone: '开放',
		sort: 'isTop,topTime,addtime',
		order: 'desc,desc,desc'
	})
	const myListQuery = ref({
		page: 1,
		limit: 20,
		parentid: 0,
		sort: 'addtime',
		order: 'desc'
	})
	const searchQuery = ref({})
	//分页
	const layouts = ref(["total","prev","pager","next"])
	const total = ref(0)
	const sizeChange = (size) => {
		listQuery.value.limit = size
		getList()
	}
	const currentChange = (page) => {
		listQuery.value.page = page
		getList()
	}
	//分页
	const searchClick = () => {
		listQuery.value.page = 1
		getList()
	}
	const getList = () => {
		listLoading.value = true
		let params = myType.value ? JSON.parse(JSON.stringify(myListQuery.value)) : JSON.parse(JSON.stringify(listQuery
			.value))
		if (searchQuery.value.title && searchQuery.value.title != '') {
			params['title'] = `%${searchQuery.value.title}%`
		}
		context?.$http({
			url: `forum/${myType.value?'page':'flist'}`,
			method: 'get',
			params: params
		}).then(res => {
			listLoading.value = false
			list.value = res.data.data.list
			total.value = res.data.data.total
		})
	}
	const userid = ref(0)
	const form = ref({
		title: '',
		isdone: '开放',
		content: '',
		parentid: 0,
		userid: userid.value,
		username: context?.$toolUtil.storageGet('frontName'),
        avatarurl:context?.$toolUtil.storageGet('headportrait')||''
	})
	const rules = ref({
		title: [{
			required: true,
			message: '请输入标题',
			trigger: 'blur'
		}, ],
		content: [{
			required: true,
			message: '请输入内容',
			trigger: 'blur'
		}, ],
	})
	const formRef = ref(null)
	const formVisible = ref(false)
	const formTitle = ref('')
	const forumShowIndex = ref(-1)
	//判断按钮显示
	const forumEnter = (index) => {
		forumShowIndex.value = index
	}
	const forumLeave = () => {
		forumShowIndex.value = -1
	}
	const resetForm = () => {
		form.value = {
			title: '',
			isdone: '开放',
			content: '',
			parentid: 0,
			userid: userid.value,
			username: context?.$toolUtil.storageGet('frontName'),
            avatarurl:context?.$toolUtil.storageGet('headportrait')||''
		}
	}
	//发布新贴
	const addClick = () => {
		resetForm()
		formTitle.value = '发布新帖'
		formVisible.value = true
	}
	//修改帖子
	const editClick = (id = null) => {
		if (id) {
			context?.$http({
				url: `forum/detail/${id}`,
				method: 'get'
			}).then(res => {
				form.value = res.data.data
				formTitle.value = '修改帖子'
				formVisible.value = true
			})
		}
	}
	//帖子内容回调
	const contentChange = (e)=>{
		form.value.content = e
	}
	//判断是否调我的发布列表
	const myType = ref(false)
	//判断是否从个人中心跳转
	const centerType = ref(false)
	//返回
	const backClick = () => {
		router.push(`/index/${context?.$toolUtil.storageGet('frontSessionTable')}Center`)
	}
	//初始化
	const init = () => {
		if (route.query.centerType) {
			centerType.value = true
		}
		if (route.query.myType) {
			myType.value = true
		}
		userid.value = context?.$toolUtil.storageGet('userid')
		getList()
	}
	const detailVisible = ref(false)
	const detailForm = ref({})
	//详情
	const detailClick = (id = null) => {
		if (id) {
			context?.$http({
				url: `forum/list/${id}`,
				method: 'get'
			}).then(res => {
				detailForm.value = res.data.data
				detailVisible.value = true
                nextTick(() => document.querySelector('.community-thread')?.scrollIntoView({behavior:'smooth',block:'start'}))
			})
		}
	}
	//删除
	const delClick = (id = null) => {
		if (id) {
			ElMessageBox.confirm(`是否删除选中帖子`, '提示', {
				confirmButtonText: '是',
				cancelButtonText: '否',
				type: 'warning',
			}).then(() => {
				context?.$http({
					url: `forum/delete`,
					method: 'post',
					data: [id]
				}).then(res => {
					context?.$toolUtil.message('删除成功', 'success', () => {
						getList()
					})
				})
			}).catch(_ => {})
		}
	}
	//发布帖子
	const save = () => {
		formRef.value.validate((valid) => {
			if (valid) {
				context?.$http({
					url: `forum/${form.value.id?'update':'add'}`,
					method: 'post',
					data: form.value
				}).then(res => {
					context?.$toolUtil.message(`${form.value.id?'修改':'发布'}成功`, 'success', () => {
						getList()
						formVisible.value = false
					})
				})
			}
		})
	}
	//评论
	const commentForm = ref({
		content: '',
		parentid: '',
		userid: '',
		username: '',
		avatarurl: '',
	})
	const commentVisible = ref(false)
	//重置评论
	const resetCommentForm = () => {
		commentForm.value = {
			content: '',
			parentid: '',
			userid: context?.$toolUtil.storageGet('userid'),
			username: context?.$toolUtil.storageGet('frontName'),
			avatarurl: context?.$toolUtil.storageGet('headportrait') ? context?.$toolUtil.storageGet('headportrait') : '',
		}
	}
	//发表评论
	const commentClick = () => {
		resetCommentForm()
		commentForm.value.parentid = detailForm.value.id
		commentVisible.value = true
	}
	//回复按钮显示
	const commentShowIndex = ref(-1)
	const commentShowIndex1 = ref(-1)
	const commentEnter = (index) => {
		commentShowIndex.value = index
	}
	const commentLeave = () => {
		commentShowIndex.value = -1
	}
	const commentEnter1 = (index) => {
		commentShowIndex1.value = index
	}
	const commentLeave1 = () => {
		commentShowIndex1.value = -1
	}
	//评论内容回调
	const commentChange = (e)=>{
		commentForm.value.content = e
	}
	//发表保存
	const commentSave = () => {
		if(commentForm.value.content==''){
			context?.$toolUtil.message(`请输入内容`, 'error')
			return false
		}
		let sensitiveWords = "";
		let sensitiveWordsArr = [];
		if(sensitiveWords) {
			sensitiveWordsArr = sensitiveWords.split(",");
		}
		for(var i=0; i<sensitiveWordsArr.length; i++){
			//全局替换
			var reg = new RegExp(sensitiveWordsArr[i],"g");
			//判断内容中是否包括敏感词
			if (commentForm.value.content.indexOf(sensitiveWordsArr[i]) > -1) {
				// 将敏感词替换为 **
				commentForm.value.content = commentForm.value.content.replace(reg,"**");
			}
		}
		context?.$http({
			url: 'forum/add',
			method: 'post',
			data: commentForm.value
		}).then(res => {
			context?.$toolUtil.message(`发表成功`, 'success', () => {
				detailClick(detailForm.value.id)
				commentVisible.value = false
			})
		})
	}
	//评论删除
	const commentDelClick = (id = null) => {
		if (id) {
			ElMessageBox.confirm(`是否删除选中评论`, '提示', {
				confirmButtonText: '是',
				cancelButtonText: '否',
				type: 'warning',
			}).then(() => {
				context?.$http({
					url: `forum/delete`,
					method: 'post',
					data: [id]
				}).then(res => {
					context?.$toolUtil.message('删除成功', 'success', () => {
						detailClick(detailForm.value.id)
					})
				})
			}).catch(_ => {})
		}
	}
	//评论回复
	const commentReplyClick = (id = null) => {
		if (id) {
			resetCommentForm()
			commentForm.value.parentid = id
			commentVisible.value = true
		}
	}
	init()
</script>


