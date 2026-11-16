<template>
	<div class="detail-page studio-detail photographer-profile" :style='{}'>
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
            <div class="photographer-gallery"><div class="photographer-main-photo"><el-image v-if="photos.length" :src="photos[selectedPhoto]" :preview-src-list="photos" :initial-index="selectedPhoto" fit="contain" preview-teleported><template #error><div class="photo-placeholder">图片暂时无法加载</div></template></el-image><el-empty v-else description="摄影师尚未上传照片"/><span v-if="photos.length" class="photo-counter">{{selectedPhoto+1}} / {{photos.length}}</span><button v-if="photos.length>1" class="photo-prev" aria-label="上一张照片" @click="selectedPhoto=(selectedPhoto+photos.length-1)%photos.length">‹</button><button v-if="photos.length>1" class="photo-next" aria-label="下一张照片" @click="selectedPhoto=(selectedPhoto+1)%photos.length">›</button></div><div v-if="photos.length>1" class="photographer-thumbnails"><button v-for="(photo,index) in photos" :key="index" :class="{selected:selectedPhoto===index}" :aria-label="'查看第'+(index+1)+'张照片'" :aria-pressed="selectedPhoto===index" @click="selectedPhoto=index"><el-image :src="photo" fit="cover"/></button></div><small v-if="photos.length">点击主图查看大图 · 使用缩略图切换照片</small></div>

			<div class="info_column">
 <div class="info_view">
  <div class="title_view"><div class="detail_title"><span>{{detail.sheyingxingming}}</span></div></div>
  <div class="info_item"><div class="info_label">摄影姓名</div><div class="info_text">{{detail.sheyingxingming}}</div></div>
  <div class="info_item"><div class="info_label">分类</div><div class="info_text">{{detail.fenlei}}</div></div>
  <div class="info_item"><div class="info_label">简介</div><div class="info_text">{{detail.jianjie}}</div></div>
  <div class="info_item"><div class="info_label">预约价格</div><div class="info_text">{{detail.yuyuejiage}}</div></div>
  <div class="info_item"><div class="info_label">工作经历</div><div class="info_text">{{detail.gongzuojingli}}</div></div>
  <div class="info_item"><div class="info_label">专业技能</div><div class="info_text">{{detail.zhuanyejineng}}</div></div>
  <div class="info_item"><div class="info_label">个人特长</div><div class="info_text">{{detail.gerentezhang}}</div></div>
  <div class="info_item"><div class="info_label">评论数</div><div class="info_text">{{detail.discussNumber}}</div></div>
  <div class="info_item" v-if="centerType"><div class="info_label">是否审核</div><div class="info_text">{{detail.sfsh}}</div></div>
  <div class="info_item" v-if="centerType"><div class="info_label">回复内容</div><div class="info_text">{{detail.shhf}}</div></div>
 </div>
 <div class="btn_view"><el-button class="cross_btn" @click="bookPhotographer" type="primary">选择档期 · 立即预约</el-button><el-button class="approval_btn" v-if="btnAuth('sheyingshixinxi','审核')&&detail.sfsh!='是'&&detail.sfsh!='否'" type="warning" @click="approvalClick()">审核</el-button><el-button class="edit_btn" v-if="centerType&&btnAuth('sheyingshixinxi','修改')" type="primary" @click="editClick">修改</el-button><el-button class="del_btn" v-if="centerType&&btnAuth('sheyingshixinxi','删除')" type="danger" @click="delClick">删除</el-button></div>
</div>
		</div>
		<section class="reviews-panel">
<div class="reviews-heading"><span class="reviews-heading-line"></span><h2>历史评价</h2><span class="reviews-heading-count">{{commentTotal}} 条</span></div>
 <section class="reviews-section">
  <header class="review-summary"><div><strong>{{commentTotal}}</strong><span>条历史评价</span></div><div class="review-stars"><el-rate :model-value="averageScore" disabled allow-half/><small>{{averageScore ? averageScore.toFixed(1) : '暂无评分'}}</small></div><el-button v-if="!commentEditing" type="primary" plain @click="startComment">写下评价</el-button></header>
  <div v-if="commentEditing" class="my_comment_view"><div class="comment-editing-head"><strong>分享你的拍摄体验</strong><el-button text @click="cancelComment">收起</el-button></div><el-form ref="commentFormRef" :model="commentForm" class="my_comment_form" :rules="commentRules"><el-form-item prop="content"><div class="review-composer"><div class="emoji-toolbar"><span class="emoji-label">添加表情</span><div class="emoji-picker"><button v-for="emoji in emojis" :key="emoji" type="button" :aria-label="'插入表情 '+emoji" @click="insertEmoji(emoji)">{{emoji}}</button></div></div><el-input ref="commentInputRef" v-model="commentForm.content" type="textarea" :rows="5" maxlength="2000" show-word-limit placeholder="说说你的拍摄体验、服务感受或作品印象…" @input="contentChange"/></div></el-form-item><el-form-item label="评分" class="rate"><el-rate v-model="commentForm.score" prop="score"/></el-form-item></el-form><div class="comment_btn"><el-button type="primary" @click="commentSave">发布评价</el-button><el-button @click="cancelComment">取消</el-button></div></div>
  <div class="comment_list"><el-empty v-if="!commentList.length" description="还没有评价，欢迎分享你的体验"/><article class="review-card" v-for="(item,index) in commentList" :key="index"><header class="comment_top"><div class="comment_user"><div class="comment_user_img"><img v-if="item.avatarurl" :src="$config.url + item.avatarurl" alt=""><img v-else src="@/assets/avatar.png" alt=""></div><div class="comment_user_info"><strong>{{item.nickname || '花海用户'}}</strong><el-tag v-if="item.bookingId" size="small" type="success">已完成拍摄</el-tag><el-rate v-if="item.score" :model-value="Number(item.score)" disabled/></div></div><time class="comment_time">{{item.addtime}}</time></header><div class="comment_bottom"><div class="comment_content" v-html="item.content"></div><div v-if="item.reviewImages" class="order-review-photos"><el-image v-for="image in item.reviewImages.split(',')" :key="image" :src="$config.url+image" :preview-src-list="item.reviewImages.split(',').map(p=>$config.url+p)" fit="cover" preview-teleported style="width:88px;height:88px;margin:12px 8px 0 0;border-radius:6px"/></div><div v-if="item.userid==user.id && !item.bookingId" class="comment_action"><el-button link type="danger" @click="commentDel(item)">删除</el-button></div><div class="comment_reply" v-if="item.reply">工作室回复：<span v-html="item.reply"></span></div></div></article></div>
  <el-pagination v-if="commentTotal>commentQuery.limit" background :layout="layouts.join(',')" :total="commentTotal" :page-size="commentQuery.limit" :current-page="commentQuery.page" @size-change="commentSizeChange" @current-change="commentCurrentChange"/>
 </section>
</section>
<el-dialog v-model="bookingVisible" title="选择拍摄档期" width="min(520px, 94vw)" class="booking-dialog" append-to-body destroy-on-close>
<div class="booking-photographer"><strong>{{detail.sheyingxingming}}</strong><span>预约价格 ¥ {{Number(detail.yuyuejiage||0).toFixed(2)}}</span></div>
<el-form ref="bookingRef" :model="booking" :rules="bookingRules" label-position="top">
<el-form-item label="拍摄日期" prop="date"><el-date-picker v-model="booking.date" type="date" value-format="YYYY-MM-DD" format="YYYY-MM-DD" :disabled-date="disabledBookingDate" placeholder="选择日期" @change="loadBookingSlots"/></el-form-item>
<el-form-item label="可选时段" prop="slot"><el-radio-group v-model="booking.slot" class="slot-options"><el-radio-button v-for="slot in bookingSlots" :key="slot.value" :label="slot.value" :disabled="slotDisabled(slot.value)">{{slot.label}}</el-radio-button></el-radio-group><el-empty v-if="booking.date && !bookingLoading && !bookingSlots.length" description="当天暂无可用时段" :image-size="50"/><el-button v-if="bookingError" text type="primary" @click="loadBookingSlots">重新查询档期</el-button></el-form-item>
<el-form-item label="联系电话" prop="phone"><el-input v-model="booking.phone" maxlength="11" placeholder="用于确认拍摄安排"/></el-form-item></el-form>
<el-alert title="提交后等待工作室确认档期和服务内容，确认后再付款。" type="info" :closable="false"/>
<template #footer><el-button @click="bookingVisible=false">取消</el-button><el-button type="primary" :loading="bookingSubmitting" :disabled="bookingLoading || bookingError" @click="submitBooking">提交预约</el-button></template></el-dialog>
<Approval ref="approvalRef" :tableName="tableName" @shChange="init()"></Approval>
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
	const tableName = 'sheyingshixinxi'
	const formName = '摄影师信息'
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
    const selectedPhoto=ref(0)
    const photos=computed(()=>bannerList.value.map(value=>value.trim()).filter(Boolean).map(value=>/^https?:/.test(value)?value:context.$config.url+value))
    watch(photos,()=>selectedPhoto.value=0)
	// 详情
	const title = ref('')
	const detail = ref({})
    const activeName = ref('commentActive')
	const getDetail = () => {
		context?.$http({
			url: `${tableName}/detail/${route.query.id}`,
			method: 'get'
		}).then(res => {
            bannerList.value = res.data.data.sheyingshizhaopian?res.data.data.sheyingshizhaopian.split(','):[]
            title.value = res.data.data.sheyingzhanghao
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
		// 评论
		getCommentList()
	}
    const contentChange = (e)=>{
        commentForm.value.content = e
    }
	//评论
	const commentForm = ref({
		content: '',
		refid: route.query.id,
		userid: context?.$toolUtil.storageGet('userid'),
		nickname: context?.$toolUtil.storageGet('frontName'),
		avatarurl: context?.$toolUtil.storageGet('headportrait') ? context?.$toolUtil.storageGet('headportrait') : '',
        score:0
	})
	const commentRules = ref({
		content: [{
			required: true,
			message: '请输入',
			trigger: 'blur'
		}, ]
	})
	const commentQuery = ref({
		page: 1,
		limit: 10,
		refid: route.query.id
	})
	const layouts = ref(["total","prev","pager","next"])
	const commentList = ref([])
	const commentEditing=ref(false),emojiVisible=ref(false),commentInputRef=ref(null)
	const emojis=['😊','😄','😍','🥰','😘','🤗','👏','👍','❤️','💖','✨','📸','🌷','🌟','🎉','☀️']
	const insertEmoji = (emoji) => {
		const editor = commentInputRef.value
		const textarea = editor && editor.$el ? editor.$el.querySelector('textarea') : null
		const text = commentForm.value.content || ''
		const start = textarea ? textarea.selectionStart : text.length
		const end = textarea ? textarea.selectionEnd : text.length
		commentForm.value.content = text.slice(0, start) + emoji + text.slice(end)
		emojiVisible.value = false
		nextTick(() => {
			const currentEditor = commentInputRef.value
			const currentTextarea = currentEditor && currentEditor.$el ? currentEditor.$el.querySelector('textarea') : null
			if (currentTextarea) {
				currentTextarea.focus()
				const caret = start + emoji.length
				currentTextarea.setSelectionRange(caret, caret)
			}
		})
	}
	const averageScore=computed(()=>{const values=commentList.value.map(item=>Number(item.score)).filter(Boolean);return values.length?values.reduce((a,b)=>a+b,0)/values.length:0})
	const commentTotal = ref(0)
	const commentFormRef = ref(null)
	const commentSizeChange = (size) =>{
		commentQuery.value.limit = size
		getCommentList()
	}
	const commentCurrentChange = (page) =>{
		commentQuery.value.page = page
		getCommentList()
	}
	const getCommentList = () => {
		context?.$http({
			url: `discuss${tableName}/list?sort=id&order=desc`,
			params: commentQuery.value,
			method: 'get'
		}).then(res => {
			commentList.value = res.data.data.list
			commentTotal.value = res.data.data.total

		})
	}
	//提交评论
	const startComment=()=>{router.push('/index/sheyingshiyuyueList?centerType=1');context.$toolUtil.message('请在已完成订单中评价摄影师','info')}
	const cancelComment=()=>{commentEditing.value=false;if(commentFormRef.value)commentFormRef.value.resetFields()}
	const commentSave = async () => {
        if(!commentForm.value.content ||commentForm.value.content=='<p><br></p>'){
            return context.$message.error("请输入评论内容")
        }
        if(!commentForm.value.score){
            return context.$message.error("评分不能为空")
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
		commentFormRef.value.validate((valid) => {
			if (valid) {
				context?.$http({
					url: `discuss${tableName}/add`,
					method: 'post',
					data: commentForm.value
				}).then(res => {
                    context.$http.get(`${tableName}/info/${detail.value.id}`).then(res=>{
                        let detail = res.data.data
                        detail.discussNumber++
                        context.$http.post(`${tableName}/update`,detail).then(()=>{
                            getDetail()
                        })
                    })
					context?.$toolUtil.message('评论成功', 'success')
                    resetForm()
                    commentEditing.value=false
                    getCommentList()
				})
			}
		})
	}
    const commentDel = (item)=>{
        context.$confirm("确定要删除该评论吗？","提示").then(()=>{
            context.$http.post(`discuss${tableName}/delete`,[item.id]).then(res=>{
                if(res.data.code==0){
                    context.$message.success("删除成功")
                    context.$http.get(`${tableName}/info/${detail.value.id}`).then(res=>{
                        let detail = res.data.data
                        detail.discussNumber--
                        context.$http.post(`${tableName}/update`,detail).then(()=>{
                            getDetail()
                        })
                    })
                    getCommentList()
                }
            })
        }).catch(()=>{})
    }
	const resetForm = () => {
		commentFormRef.value.resetFields()
	}
	//审核
	import Approval from '@/components/approval.vue'
	const approvalRef = ref(null)
	const approvalClick = (btnType='审核') => {
		if(!context?.$toolUtil.storageGet('frontToken')){
			context?.$toolUtil.message('请登录后再操作！','error')
			return false
		}
		if(!btnAuth('sheyingshixinxi',btnType)){
			context?.$toolUtil.message('暂无权限操作！','error')
			return false
		}
		let row = detail.value
		let params = {
			id: row.id,
			sfsh: row.sfsh,
			shhf: row.shhf,
		}
		nextTick(() => {
			approvalRef.value.approvalClick(params)
		})
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
    const bookingVisible=ref(false),bookingSubmitting=ref(false),bookingLoading=ref(false),bookingError=ref(false),bookingRef=ref(null),booking=ref({date:'',slot:'',phone:''}),bookingSlots=ref([])
    const bookingRules={date:[{required:true,message:'请选择拍摄日期',trigger:'change'}],slot:[{required:true,message:'请选择拍摄时段',trigger:'change'}],phone:[{required:true,message:'请输入联系电话',trigger:'blur'},{pattern:/^1[3-9]\d{9}$/,message:'请输入正确的手机号',trigger:'blur'}]}
    const disabledBookingDate=time=>{const now=new Date();now.setHours(0,0,0,0);return time<now}
    const slotDisabled=value=>bookingSlots.value.find(x=>x.value===value)?.disabled || (booking.value.date && new Date(booking.value.date+'T'+value.slice(0,5)+':00').getTime()<=Date.now())
    const loadBookingSlots=async()=>{booking.value.slot='';bookingSlots.value=[];bookingError.value=false;if(!booking.value.date)return;bookingLoading.value=true;try{const result=await context.$http.get('sheyingshiyuyue/availability',{params:{sheyingzhanghao:detail.value.sheyingzhanghao,date:booking.value.date}});const all=['08:00-09:00','09:00-10:00','10:00-11:00','11:00-12:00','13:00-14:00','14:00-15:00','15:00-16:00','16:00-17:00'];const occupied=result.data.data;bookingSlots.value=all.map(value=>({value,label:value,disabled:occupied.includes(value)}))}catch{bookingError.value=true;context.$toolUtil.message('档期查询失败，请重试','error')}finally{bookingLoading.value=false}}
    const bookPhotographer=()=>{if(!context.$toolUtil.storageGet('frontToken')){context.$toolUtil.storageSet('toPath',route.fullPath);router.push('/login');return}if(context.$toolUtil.storageGet('frontSessionTable')!=='yonghu'){context.$toolUtil.message('请使用用户账号预约摄影师','warning');return}booking.value={date:'',slot:'',phone:user.value?.lianxidianhua||''};bookingSlots.value=[];bookingError.value=false;bookingVisible.value=true}
    const submitBooking=async()=>{if(bookingSubmitting.value)return;try{await bookingRef.value.validate()}catch{return}bookingSubmitting.value=true;try{await context.$http.post('sheyingshiyuyue/add',{sheyingzhanghao:detail.value.sheyingzhanghao,yuyuesheyingriqi:booking.value.date,yuyueshijianduan:booking.value.slot,lianxidianhua:booking.value.phone});bookingVisible.value=false;context.$toolUtil.message('预约已提交，请等待工作室确认','success')}catch(error){context.$toolUtil.message(error?.data?.msg||'预约提交失败，请重试','error')}finally{bookingSubmitting.value=false}}

    const sheyingshiyuyueonAcross = async (btnType,crossOptAudit,crossOptPay,statusColumnName,tips,statusColumnValue,type=1) => {
		if(!context?.$toolUtil.storageGet('frontToken')){
			context?.$toolUtil.message('请登录后再操作！','error')
			return false
		}
		if(!btnAuth('sheyingshixinxi',btnType)){
			context?.$toolUtil.message('暂无权限操作！','error')
			return false
		}
		if(crossOptAudit=='是'&&detail.value.sfsh!='是') {
			context?.$toolUtil.message('请审核通过后再操作！','error')
			return false
		}
		context?.$toolUtil.storageSet('crossObj',JSON.stringify(detail.value))
		context?.$toolUtil.storageSet('crossTable',tableName)
		context?.$toolUtil.storageSet('crossStatusColumnName',statusColumnName)
		context?.$toolUtil.storageSet('crossTips',tips)
		context?.$toolUtil.storageSet('crossStatusColumnValue',statusColumnValue)
		if(statusColumnName!=''&&!statusColumnName.startsWith("[")) {
			var obj = detail.value
			for (var o in obj){
				if(o==statusColumnName && obj[o]==statusColumnValue){
					context?.$toolUtil.message(tips,'warning')
					return;
				}
			}
		}
		nextTick(()=>{
			router.push(`/index/sheyingshiyuyueAdd?type=cross&&id=${detail.value.id}`)
		})
	}
	onMounted(()=>{
		init()
	})
</script>

<style scoped>
.studio-detail .detail_view .info_view{width:100%!important;min-width:0!important;padding:0!important;background:none!important;border:0!important;box-shadow:none!important}.studio-detail .detail_view .info_view .title_view{padding:0!important;margin:0 0 24px!important;background:none!important}.studio-detail .detail_view .info_view .detail_title{font-size:32px!important;line-height:1.6!important;font-weight:400!important;color:#294735!important;padding:0!important;text-align:left!important}.studio-detail .detail_view .info_view .info_item{display:block!important;width:100%!important;padding:16px 0!important;margin:0!important;background:none!important;border:0!important;border-bottom:1px solid #e2e6db!important}.studio-detail .detail_view .info_view .info_label{display:block!important;width:auto!important;padding:0!important;margin:0 0 7px!important;font-size:11px!important;color:#8b967f!important;text-align:left!important;line-height:1.6!important}.studio-detail .detail_view .info_view .info_text{display:block!important;width:auto!important;padding:0!important;margin:0!important;font-size:14px!important;color:#53644c!important;line-height:1.95!important;overflow-wrap:anywhere!important}.studio-detail .detail_view .btn_view .cross_btn{background:#305d4c!important;border-color:#305d4c!important;color:white!important}.studio-detail :deep(.el-image__inner){width:100%!important;height:100%!important;object-fit:cover!important}@media(max-width:560px){.studio-detail .detail_view .info_view .detail_title{font-size:27px!important}}
</style>
<style>
.photographer-profile.studio-detail{max-width:1240px;margin:0 auto;padding:30px 28px 60px}
.photographer-profile.studio-detail .detail_view{display:grid;grid-template-columns:minmax(0,1.05fr) minmax(0,1fr);gap:40px;align-items:start;background:none;border:0;box-shadow:none;padding:0}
.photographer-profile .photographer-gallery{min-width:0;position:relative}.photographer-profile.studio-detail .info_view{position:static;align-self:start;max-height:none;overflow:visible}
.photographer-main-photo{height:460px;background:#eef0e9;border-radius:12px;overflow:hidden;position:relative;display:flex;align-items:center;justify-content:center}
.photographer-main-photo>.el-image{width:100%;height:100%;display:block}
.photographer-main-photo>.el-image img{object-fit:contain}
.photographer-gallery .photo-counter{position:absolute;bottom:16px;right:16px;background:#243e32b8;color:#fff;border-radius:16px;padding:5px 12px;font-size:12px}
.photographer-gallery .photo-prev,.photographer-gallery .photo-next{position:absolute;top:45%;width:36px;height:44px;border:0;border-radius:6px;background:#fffdf0dc;color:#305d4c;font-size:28px;cursor:pointer}
.photographer-gallery .photo-prev{left:12px}.photographer-gallery .photo-next{right:12px}
.photographer-thumbnails{display:flex;gap:10px;overflow:auto;padding:14px 0 6px}
.photographer-thumbnails>button{flex:0 0 76px;height:76px;border:2px solid transparent;border-radius:8px;padding:3px;background:#fff;cursor:pointer;overflow:hidden}
.photographer-thumbnails>button.selected{border-color:#305d4c}.photographer-thumbnails .el-image{width:100%;height:100%;border-radius:4px}
.photographer-gallery>small{display:block;font-size:11px;color:#909c88;margin-top:10px}
.photographer-profile.studio-detail .info_view{width:100%;min-width:0;padding:4px 0;margin:0;background:none;box-shadow:none;border:0}
.photographer-profile.studio-detail .info_item{padding:15px 0;border-bottom:1px solid #e4e8dd;display:block}
.photographer-profile.studio-detail .info_label{font-size:12px;color:#8d9984;margin-bottom:8px;width:auto}
.photographer-profile.studio-detail .info_text{font-size:14px;line-height:1.9;width:auto;white-space:pre-line}
.photographer-profile.studio-detail .detail_title{font-size:30px;margin:0 0 12px}
.photographer-profile.studio-detail .btn_view{margin-top:24px;padding:0;display:flex;gap:10px;flex-wrap:wrap}
.photo-placeholder{display:grid;place-items:center;height:100%;color:#8a9580}
@media(max-width:800px){.photographer-profile.studio-detail .detail_view{grid-template-columns:1fr;gap:28px}.photographer-profile .photographer-gallery{position:static}.photographer-main-photo{height:380px}}
@media(max-width:500px){.photographer-profile.studio-detail{padding:20px 16px 40px}.photographer-main-photo{height:320px}.photographer-thumbnails>button{flex-basis:64px;height:64px}}
</style><style>
.reviews-section{max-width:1000px;margin:0 auto}.review-summary{display:flex;align-items:center;gap:24px;padding:18px 22px;background:#f6f8f3;border:1px solid #e8ece3;border-radius:10px}.review-summary>div:first-child{display:flex;align-items:baseline;gap:8px}.review-summary>div:first-child strong{font-size:24px;color:#305d4c}.review-summary>div:first-child span,.review-stars small{font-size:12px;color:#829078}.review-stars{display:flex;align-items:center;gap:9px;margin-right:auto}.emoji-toolbar{display:flex;align-items:center;gap:10px;margin-bottom:10px;padding:8px 10px;background:#f7faf6;border:1px solid #e4ebe2;border-radius:8px}.emoji-label{font-size:12px;color:#71806d;white-space:nowrap}.emoji-picker{display:flex;flex-wrap:wrap;gap:3px}.emoji-picker button{border:0;background:transparent;border-radius:6px;font-size:21px;line-height:1;width:34px;height:32px;cursor:pointer;transition:background .2s,transform .2s}.emoji-picker button:hover{background:#e7f1e9;transform:translateY(-1px)}.review-card{padding:20px 4px;border-bottom:1px solid #edf0e8}.review-card .comment_top{display:flex;align-items:center;justify-content:space-between}.review-card .comment_user{display:flex;align-items:center;gap:12px}.review-card .comment_user_img{width:40px;height:40px;border-radius:50%;overflow:hidden}.review-card .comment_user_img img{width:100%;height:100%;object-fit:cover}.review-card .comment_user_info{display:flex;flex-direction:column;gap:5px}.review-card .comment_time{font-size:11px;color:#9aa394}.review-card .comment_bottom{padding:14px 0 0 52px}.review-card .comment_content{font-size:14px;line-height:1.9;color:#53644c;overflow-wrap:anywhere}.review-card .comment_reply{margin-top:12px;padding:12px 14px;background:#f6f8f3;border-radius:6px;color:#71806d;font-size:12px}.my_comment_view{padding:20px;margin:16px 0;background:#f8faf6;border:1px solid #e6ebdf;border-radius:10px}.comment-editing-head{display:flex;justify-content:space-between;align-items:center;margin-bottom:14px}.my_comment_view .el-textarea__inner{min-height:130px!important;resize:vertical;border-radius:8px}.booking-photographer{display:flex;justify-content:space-between;align-items:center;padding:14px 16px;background:#f6f8f3;border-radius:8px;margin-bottom:20px;color:#50624c}.booking-photographer span{color:#305d4c;font-size:14px}.booking-dialog .el-date-editor{width:100%}.slot-options{display:flex;flex-wrap:wrap;gap:8px}.slot-options .el-radio-button{margin:0}.slot-options .el-radio-button__inner{border-left:1px solid var(--el-border-color)!important;border-radius:6px!important}.slot-options .el-radio-button.is-active .el-radio-button__inner{background:#305d4c;border-color:#305d4c;box-shadow:none}.booking-dialog .el-form-item{margin-bottom:18px}
</style><style>
/* 评价区采用轻量分栏布局，减少整块浅色背景带来的嵌套感 */
.photographer-profile .tabs_view{margin-top:46px;background:transparent!important;border:0!important;box-shadow:none!important}
.photographer-profile .tabs_view :deep(.el-tabs__header){margin:0 0 18px;padding:0 0 10px;border-bottom:1px solid #e8ece6}
.photographer-profile .tabs_view :deep(.el-tabs__nav-wrap::after){display:none}
.photographer-profile .tabs_view :deep(.el-tabs__item){height:38px;padding:0 18px!important;color:#7d8878;font-size:14px;font-weight:600}
.photographer-profile .tabs_view :deep(.el-tabs__item.is-active){color:#305d4c}
.photographer-profile .tabs_view :deep(.el-tabs__active-bar){height:3px;border-radius:3px;background:#55b8ab}
.photographer-profile .reviews-section{max-width:1000px;margin:0 auto}
.photographer-profile .review-summary{display:grid;grid-template-columns:minmax(150px,auto) 1fr auto;align-items:center;gap:26px;padding:20px 24px;background:#fff;border:1px solid #e6ebe5;border-radius:14px;box-shadow:0 8px 24px rgba(45,76,58,.06)}
.photographer-profile .review-summary>div:first-child{display:flex;align-items:baseline;gap:9px;padding-right:26px;border-right:1px solid #edf1ec}
.photographer-profile .review-summary>div:first-child strong{font-size:30px;line-height:1;color:#305d4c;font-weight:700}
.photographer-profile .review-summary>div:first-child span{font-size:13px;color:#839080}
.photographer-profile .review-stars{display:flex;align-items:center;gap:10px;margin-right:0}
.photographer-profile .review-stars :deep(.el-rate){height:24px}
.photographer-profile .review-stars small{font-size:13px;color:#6f7e6e}
.photographer-profile .review-summary>.el-button{min-width:104px;height:38px;border-radius:8px;font-weight:600}
.photographer-profile .comment_list{margin-top:18px;padding:0 24px;background:#fff;border:1px solid #edf0eb;border-radius:14px}
.photographer-profile .comment_list :deep(.el-empty){padding:58px 0 62px}
.photographer-profile .review-card{padding:22px 0;border-bottom:1px solid #edf0eb}
.photographer-profile .review-card:last-child{border-bottom:0}
.photographer-profile .my_comment_view{margin:18px 0;padding:22px 24px;background:#fff;border:1px solid #e5ebe3;border-radius:14px;box-shadow:0 8px 24px rgba(45,76,58,.05)}
@media(max-width:800px){.photographer-profile.studio-detail .info_view{position:static;max-height:none;overflow:visible}.photographer-profile .review-summary{grid-template-columns:1fr auto;gap:16px;padding:18px}.photographer-profile .review-summary>div:first-child{border-right:0;padding-right:0}.photographer-profile .review-stars{grid-column:1/-1;grid-row:2}.photographer-profile .review-summary>.el-button{grid-column:2;grid-row:1}.photographer-profile .comment_list{padding:0 18px}}
@media(max-width:480px){.photographer-profile .review-summary{display:flex;flex-wrap:wrap}.photographer-profile .review-summary>.el-button{margin-left:auto}.photographer-profile .review-stars{width:100%}}
</style><style>
.photographer-profile .my_comment_view{width:100%;box-sizing:border-box}
.photographer-profile .review-composer{width:100%}
.photographer-profile .my_comment_view .el-textarea__inner{min-height:190px!important;width:100%;padding:16px 18px;font-size:15px;line-height:1.8}
.photographer-profile .my_comment_view .el-form-item{width:100%}
.photographer-profile .my_comment_view .el-form-item__content{display:block;width:100%}
@media(max-width:600px){.photographer-profile .my_comment_view .el-textarea__inner{min-height:160px}.emoji-toolbar{align-items:flex-start}.emoji-picker button{width:30px}}
</style><style>
.photographer-profile .reviews-panel{max-width:1000px;margin:46px auto 0}
.photographer-profile .reviews-heading{display:flex;align-items:center;gap:10px;margin:0 0 18px;padding:0 2px}
.photographer-profile .reviews-heading-line{width:4px;height:22px;border-radius:4px;background:#55b8ab}
.photographer-profile .reviews-heading h2{margin:0;color:#385640;font-size:21px;font-weight:700}
.photographer-profile .reviews-heading-count{margin-left:auto;color:#98a394;font-size:13px}
.photographer-profile .photo-booking{position:absolute;left:18px;bottom:18px;z-index:3;padding:10px 18px;border:1px solid rgba(255,255,255,.65);border-radius:8px;background:rgba(48,93,76,.78);backdrop-filter:blur(4px);color:#fff;font-size:14px;font-weight:600;cursor:pointer;transition:background .2s,transform .2s}
.photographer-profile .photo-booking:hover{background:rgba(48,93,76,.92);transform:translateY(-1px)}
@media(max-width:600px){.photographer-profile .reviews-panel{margin-top:34px}.photographer-profile .photo-booking{left:12px;bottom:12px;padding:9px 14px;font-size:13px}}
</style><style>
/* 玻璃拟态预约按钮，保留照片主体的可见度 */
.photographer-profile .photo-booking{padding:11px 22px;border:1px solid rgba(255,255,255,.82);border-radius:10px;background:rgba(255,255,255,.18);backdrop-filter:blur(12px) saturate(135%);-webkit-backdrop-filter:blur(12px) saturate(135%);box-shadow:0 6px 18px rgba(20,45,35,.2),inset 0 1px 0 rgba(255,255,255,.45);color:#fff;text-shadow:0 1px 3px rgba(0,0,0,.3);font-size:14px;font-weight:700;letter-spacing:.5px}
.photographer-profile .photo-booking:hover{background:rgba(255,255,255,.3);box-shadow:0 8px 22px rgba(20,45,35,.26),inset 0 1px 0 rgba(255,255,255,.6);transform:translateY(-2px)}
.photographer-profile .reviews-panel{width:100%;max-width:1100px}
.photographer-profile .reviews-section{max-width:none;width:100%}
.photographer-profile .review-summary{min-height:76px;padding:22px 28px}
.photographer-profile .comment_list{width:100%;box-sizing:border-box}
@media(max-width:600px){.photographer-profile .photo-booking{padding:9px 16px}.photographer-profile .review-summary{padding:19px 18px}}
</style><style>
/* 详情两列的视觉起点对齐，减少左侧缩略图下方的空白感 */
.photographer-profile.studio-detail .detail_view{grid-template-columns:minmax(0,1fr) minmax(420px,1fr);gap:34px}
.photographer-profile .photographer-gallery{padding-bottom:0}
.photographer-profile .photographer-thumbnails{margin-bottom:0}
.photographer-profile.studio-detail .info_view{padding-top:78px!important}
/* 预约按钮独立悬浮，不跟随简介内容排版 */
.photographer-profile.studio-detail .info_view .btn_view{position:fixed;right:clamp(24px,5vw,84px);bottom:26px;z-index:20;margin:0;padding:12px 14px;background:rgba(255,255,255,.78);border:1px solid rgba(220,229,221,.9);border-radius:12px;box-shadow:0 10px 28px rgba(36,62,50,.15);backdrop-filter:blur(12px);-webkit-backdrop-filter:blur(12px)}
.photographer-profile.studio-detail .info_view .btn_view .approval_btn,.photographer-profile.studio-detail .info_view .btn_view .edit_btn,.photographer-profile.studio-detail .info_view .btn_view .del_btn{display:none}
@media(max-width:800px){.photographer-profile.studio-detail .detail_view{grid-template-columns:1fr}.photographer-profile.studio-detail .info_view{padding-top:0!important}.photographer-profile.studio-detail .info_view .btn_view{right:16px;left:16px;bottom:16px;justify-content:center}}
</style><style>
/* 修正详情页布局，预约操作回到右侧信息底部 */
.photographer-profile.studio-detail .detail_view{grid-template-columns:minmax(0,1.05fr) minmax(0,1fr);gap:40px}
.photographer-profile.studio-detail .info_view{padding-top:4px!important;position:static!important;max-height:none!important;overflow:visible!important}
.photographer-profile.studio-detail .info_view .btn_view{position:static!important;display:flex!important;justify-content:flex-start;align-items:center;gap:10px;margin-top:24px!important;padding:0!important;background:transparent!important;border:0!important;border-radius:0!important;box-shadow:none!important;backdrop-filter:none!important}
.photographer-profile.studio-detail .info_view .btn_view .approval_btn,.photographer-profile.studio-detail .info_view .btn_view .edit_btn,.photographer-profile.studio-detail .info_view .btn_view .del_btn{display:inline-flex}
@media(max-width:800px){.photographer-profile.studio-detail .info_view{padding-top:0!important}.photographer-profile.studio-detail .info_view .btn_view{position:static!important;padding:0!important}}
</style><style>
/* 右侧简介与左侧主图、缩略图形成同高信息面板 */
.photographer-profile.studio-detail .info_view{height:590px!important;max-height:590px!important;overflow-y:auto!important;overflow-x:hidden!important;padding:4px 10px 18px 0!important;box-sizing:border-box;scrollbar-width:thin;scrollbar-color:#c8d7cd transparent}
.photographer-profile.studio-detail .info_view .btn_view{position:sticky!important;bottom:0;z-index:2;padding:14px 0 4px!important;background:linear-gradient(to bottom,rgba(255,255,255,0),#fff 24%)!important}
@media(max-width:800px){.photographer-profile.studio-detail .info_view{height:auto!important;max-height:none!important;overflow:visible!important;padding:0!important}.photographer-profile.studio-detail .info_view .btn_view{position:static!important;background:transparent!important;padding:0!important}}
</style><style>
/* 取消信息区固定高度，避免内容不足时出现空白底 */
.photographer-profile.studio-detail .info_view{height:auto!important;max-height:none!important;overflow:visible!important;padding:4px 0 0!important}
.photographer-profile.studio-detail .info_view .btn_view{position:static!important;background:transparent!important;padding:0!important;margin-top:24px!important}
/* 评价改为无卡片边框的开放式列表 */
.photographer-profile .comment_list{margin-top:18px;padding:0;background:transparent;border:0;border-radius:0;box-shadow:none}
.photographer-profile .review-card{padding:22px 0;border:0;border-bottom:1px solid #edf0e8;background:transparent;border-radius:0}
.photographer-profile .review-card:last-child{border-bottom:0}
.photographer-profile .review-card .comment_bottom{padding-left:52px}
</style><style>
/* 简介最多与左侧主图和缩略图同高，内容不足时不制造空白 */
.photographer-profile.studio-detail .info_view{height:auto!important;max-height:590px!important;overflow-y:auto!important;overflow-x:hidden!important;padding:4px 10px 12px 0!important;box-sizing:border-box;scrollbar-width:thin;scrollbar-color:#c8d7cd transparent}
.photographer-profile.studio-detail .info_view .btn_view{position:sticky!important;bottom:0;z-index:2;padding:14px 0 4px!important;background:linear-gradient(to bottom,rgba(255,255,255,0),#fff 24%)!important}
@media(max-width:800px){.photographer-profile.studio-detail .info_view{max-height:none!important;overflow:visible!important;padding:0!important}.photographer-profile.studio-detail .info_view .btn_view{position:static!important;background:transparent!important;padding:0!important}}
</style><style>
/* 预约按钮区域与页面背景一致，移除底部白色渐变遮罩 */
.photographer-profile.studio-detail .info_view .btn_view{background:transparent!important;background-image:none!important;box-shadow:none!important;border:0!important}
.photographer-profile.studio-detail .info_view .btn_view::before,.photographer-profile.studio-detail .info_view .btn_view::after{display:none!important;content:none!important}
</style><style>
/* 最终布局：按钮紧跟简介末尾，绝不覆盖正文 */
.photographer-profile.studio-detail .info_view{max-height:590px!important;overflow-y:auto!important;overflow-x:hidden!important}
.photographer-profile.studio-detail .info_view .btn_view{position:static!important;display:flex!important;flex-shrink:0!important;clear:both!important;margin-top:24px!important;padding:0 0 8px!important;background:transparent!important;background-image:none!important;box-shadow:none!important;border:0!important}
@media(max-width:800px){.photographer-profile.studio-detail .info_view{max-height:none!important;overflow:visible!important}.photographer-profile.studio-detail .info_view .btn_view{position:static!important;margin-top:24px!important;padding:0!important}}
</style><style>
/* 简介是独立滚动区，预约操作作为其下方的独立区块 */
.photographer-profile.studio-detail .detail_view{align-items:start}
.photographer-profile.studio-detail .info_column{display:flex;flex-direction:column;align-self:start;min-width:0}
.photographer-profile.studio-detail .info_column .info_view{height:auto!important;max-height:590px!important;overflow-y:auto!important;overflow-x:hidden!important;padding:4px 10px 8px 0!important;box-sizing:border-box;scrollbar-width:thin}
.photographer-profile.studio-detail .info_column .btn_view{position:static!important;display:flex!important;flex:none;align-items:center;justify-content:flex-start;gap:10px;margin:16px 0 0!important;padding:0!important;background:none!important;background-image:none!important;border:0!important;border-radius:0!important;box-shadow:none!important;backdrop-filter:none!important}
@media(max-width:800px){.photographer-profile.studio-detail .info_column .info_view{max-height:none!important;overflow:visible!important;padding:0!important}.photographer-profile.studio-detail .info_column .btn_view{position:static!important;margin-top:18px!important}}
</style>