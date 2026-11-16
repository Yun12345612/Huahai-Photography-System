	import {
		createRouter,
		createWebHashHistory
	} from 'vue-router'
	import discusssheyingshixinxi from '@/views/discusssheyingshixinxi/list'
	import sheyingshi from '@/views/sheyingshi/list'
	import sheyingshiyuyue from '@/views/sheyingshiyuyue/list'
	import BookingCalendar from '@/views/BookingCalendar.vue'
import BookingChanges from '@/views/BookingChanges.vue'
	import huodongxinxi from '@/views/huodongxinxi/list'
	import users from '@/views/users/list'
	import fenlei from '@/views/fenlei/list'
	import forum from '@/views/forum/list'
	import sheyingshixinxi from '@/views/sheyingshixinxi/list'
	import baomingxinxi from '@/views/baomingxinxi/list'
	import chat from '@/views/chat/list'
	import yonghu from '@/views/yonghu/list'
	import friendshipLink from '@/views/friendshipLink/list'
	import sheyingzuopin from '@/views/sheyingzuopin/list'
	import config from '@/views/config/list'
	import usersCenter from '@/views/users/center'
	import sheyingshiCenter from '@/views/sheyingshi/center'
import OperationLogs from '@/views/OperationLogs.vue'

export const routes = [{
		path: '/login',
		name: 'login',
        meta: { title: '登录' },
		component: () => import('../views/login.vue')
	},{
		path: '/',
		name: '首页',
        meta: { title: '首页' },
		component: () => import('../views/index'),
		children: [{
			path: '/',
			name: 'home',
			component: () => import('../views/StudioDashboard.vue'),
			meta: {
				affix: true,
                title: '首页'
			}
		}, {
			path: '/updatepassword',
			name: 'updatepassword',
            meta: { title: '修改密码' },
			component: () => import('../views/updatepassword.vue')
		}

		,{
			path: '/usersCenter',
			name: 'usersCenter',
            meta: { title: '管理员个人中心' },
			component: usersCenter
		}
		,{
			path: '/sheyingshiCenter',
			name: 'sheyingshiCenter',
            meta: { title: '摄影师个人中心' },
			component: sheyingshiCenter
		}
		,{
			path: '/discusssheyingshixinxi',
			name: 'discusssheyingshixinxi',
            meta: { title: '摄影师信息评论-评论' },
			component: discusssheyingshixinxi
		}
		,{
			path: '/sheyingshi',
			name: 'sheyingshi',
            meta: { title: '摄影师' },
			component: sheyingshi
		}
		,{
			path: '/sheyingshiyuyue',
			name: 'sheyingshiyuyue',
            meta: { title: '摄影师预约' },
			component: sheyingshiyuyue
		}
		,{
				path: '/operation-logs', name: 'operation-logs', meta: { title: '操作日志' }, component: OperationLogs
            },{
                path: '/message-center', name: 'message-center', meta: { title: '消息中心' }, component: () => import('@/views/StaffMessages.vue')
            },{
                path: '/booking-refunds',
                name: 'booking-refunds',
                meta: { title: '退款管理' },
                component: () => import('@/views/BookingRefunds.vue')
            },{
                path: '/booking-changes',
				name: 'booking-changes',
				meta: { title: '预约变更申请' },
				component: BookingChanges
			}
		,{
				path: '/booking-calendar',
				name: 'booking-calendar',
				meta: { title: '预约档期日历' },
				component: BookingCalendar
			}
		,{
				path: '/huodongxinxi',
			name: 'huodongxinxi',
            meta: { title: '活动信息' },
			component: huodongxinxi
		}
		,{
			path: '/users',
			name: 'users',
            meta: { title: '管理员' },
			component: users
		}
		,{
			path: '/fenlei',
			name: 'fenlei',
            meta: { title: '分类' },
			component: fenlei
		}
		,{
			path: '/forum',
			name: 'forum',
            meta: { title: '我的发布' },
			component: forum
		}
		,{
			path: '/sheyingshixinxi',
			name: 'sheyingshixinxi',
            meta: { title: '摄影师信息' },
			component: sheyingshixinxi
		}
		,{
			path: '/baomingxinxi',
			name: 'baomingxinxi',
            meta: { title: '报名信息' },
			component: baomingxinxi
		}
		,{
			path: '/chat',
			name: 'chat',
            meta: { title: '在线咨询' },
			component: chat
		}
		,{
			path: '/yonghu',
			name: 'yonghu',
            meta: { title: '用户' },
			component: yonghu
		}
		,{
			path: '/friendshipLink',
			name: 'friendshipLink',
            meta: { title: '友情链接' },
			component: friendshipLink
		}
		,{
			path: '/sheyingzuopin',
			name: 'sheyingzuopin',
            meta: { title: '摄影作品' },
			component: sheyingzuopin
		}
		,{
			path: '/config',
			name: 'config',
            meta: { title: '轮播图' },
			component: config
		}
		]
	},
]

const router = createRouter({
	history: createWebHashHistory(process.env.BASE_URL),
	routes
})

router.beforeEach(to => {
 if(to.path!=='/login'&&!localStorage.getItem('Token'))return '/login'
 if(to.path==='/operation-logs'&&localStorage.getItem('sessionTable')!=='users')return '/'
 return true
})
export default router


