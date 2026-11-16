import { createRouter, createWebHashHistory } from 'vue-router'
import index from '../views'
import home from '../views/pages/home.vue'
import login from '../views/pages/login.vue'
import forumList from '@/views/pages/forum/list'
import yonghuList from '@/views/pages/yonghu/list'
import yonghuDetail from '@/views/pages/yonghu/formModel'
import yonghuAdd from '@/views/pages/yonghu/formAdd'
import yonghuRegister from '@/views/pages/yonghu/register'
import sheyingshiRegister from '@/views/pages/sheyingshi/register'
import yonghuCenter from '@/views/pages/yonghu/center'
import messageCenter from '@/views/pages/messageCenter.vue'
import sheyingshiList from '@/views/pages/sheyingshi/list'
import sheyingshiDetail from '@/views/pages/sheyingshi/formModel'
import sheyingshiAdd from '@/views/pages/sheyingshi/formAdd'
import sheyingshixinxiList from '@/views/pages/sheyingshixinxi/list'
import sheyingshixinxiDetail from '@/views/pages/sheyingshixinxi/formModel'
import sheyingshixinxiAdd from '@/views/pages/sheyingshixinxi/formAdd'
import sheyingshiyuyueList from '@/views/pages/sheyingshiyuyue/list'
import sheyingshiyuyueDetail from '@/views/pages/sheyingshiyuyue/formModel'
import sheyingshiyuyueAdd from '@/views/pages/sheyingshiyuyue/formAdd'
import sheyingzuopinList from '@/views/pages/sheyingzuopin/list'
import sheyingzuopinDetail from '@/views/pages/sheyingzuopin/formModel'
import sheyingzuopinAdd from '@/views/pages/sheyingzuopin/formAdd'
import baomingxinxiList from '@/views/pages/baomingxinxi/list'
import baomingxinxiDetail from '@/views/pages/baomingxinxi/formModel'
import baomingxinxiAdd from '@/views/pages/baomingxinxi/formAdd'
import huodongxinxiList from '@/views/pages/huodongxinxi/list'
import huodongxinxiDetail from '@/views/pages/huodongxinxi/formModel'
import huodongxinxiAdd from '@/views/pages/huodongxinxi/formAdd'
import fenleiList from '@/views/pages/fenlei/list'
import fenleiDetail from '@/views/pages/fenlei/formModel'
import fenleiAdd from '@/views/pages/fenlei/formAdd'
import friendshipLinkList from '@/views/pages/friendshipLink/list'
import friendshipLinkDetail from '@/views/pages/friendshipLink/formModel'
import friendshipLinkAdd from '@/views/pages/friendshipLink/formAdd'

const routes = [{
		path: '/',
		redirect: '/index/home'
	},
	{
		path: '/index',
		component: index,
		children: [{
			path: 'home',
			component: home
		}
		, {
			path: 'forumList',
			component: forumList
		}
		, {
			path: 'yonghuList',
			component: yonghuList
		}, {
			path: 'yonghuDetail',
			component: yonghuDetail
		}, {
			path: 'yonghuAdd',
			component: yonghuAdd
		}
		, {
			path: 'messageCenter',
			component: messageCenter
		}, {
			path: 'yonghuCenter',
			component: yonghuCenter
		}
		, {
			path: 'sheyingshiList',
			component: sheyingshiList
		}, {
			path: 'sheyingshiDetail',
			component: sheyingshiDetail
		}, {
			path: 'sheyingshiAdd',
			component: sheyingshiAdd
		}
		, {
			path: 'sheyingshixinxiList',
			component: sheyingshixinxiList
		}, {
			path: 'sheyingshixinxiDetail',
			component: sheyingshixinxiDetail
		}, {
			path: 'sheyingshixinxiAdd',
			component: sheyingshixinxiAdd
		}
		, {
			path: 'sheyingshiyuyueList',
			component: sheyingshiyuyueList
		}, {
			path: 'sheyingshiyuyueDetail',
			component: sheyingshiyuyueDetail
		}, {
			path: 'sheyingshiyuyueAdd',
			component: sheyingshiyuyueAdd
		}
		, {
			path: 'sheyingzuopinList',
			component: sheyingzuopinList
		}, {
			path: 'sheyingzuopinDetail',
			component: sheyingzuopinDetail
		}, {
			path: 'sheyingzuopinAdd',
			component: sheyingzuopinAdd
		}
		, {
			path: 'baomingxinxiList',
			component: baomingxinxiList
		}, {
			path: 'baomingxinxiDetail',
			component: baomingxinxiDetail
		}, {
			path: 'baomingxinxiAdd',
			component: baomingxinxiAdd
		}
		, {
			path: 'huodongxinxiList',
			component: huodongxinxiList
		}, {
			path: 'huodongxinxiDetail',
			component: huodongxinxiDetail
		}, {
			path: 'huodongxinxiAdd',
			component: huodongxinxiAdd
		}
		, {
			path: 'fenleiList',
			component: fenleiList
		}, {
			path: 'fenleiDetail',
			component: fenleiDetail
		}, {
			path: 'fenleiAdd',
			component: fenleiAdd
		}
		, {
			path: 'friendshipLinkList',
			component: friendshipLinkList
		}, {
			path: 'friendshipLinkDetail',
			component: friendshipLinkDetail
		}, {
			path: 'friendshipLinkAdd',
			component: friendshipLinkAdd
		}
		]
	},
	{
		path: '/login',
		component: login
	}
	,{
		path: '/yonghuRegister',
		component: yonghuRegister
	},
	{
		path: '/sheyingshiRegister',
		component: sheyingshiRegister
	}
]

const router = createRouter({
  history: createWebHashHistory(process.env.BASE_URL),
  routes
})

export default router
