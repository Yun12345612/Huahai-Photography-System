const config = {
    get() {
        return {
            url : process.env.VUE_APP_BASE_API_URL + process.env.VUE_APP_BASE_API + '/',
            name: process.env.VUE_APP_BASE_API,
			menuList:[
				{
					name: '活动信息管理',
					icon: '',
					child:[

						{
							name:'活动信息',
                            url:'/index/huodongxinxiList'

						},
					]
				},
				{
					name: '摄影师信息管理',
					icon: '',
					child:[

						{
							name:'摄影师信息',
                            url:'/index/sheyingshixinxiList'

						},
					]
				},
				{
					name: '摄影作品管理',
					icon: '',
					child:[

						{
							name:'摄影作品',
                            url:'/index/sheyingzuopinList'

						},
					]
				},
				{
					name: '论坛交流',
					icon: '',
					child:[

						{
							name:'互动社交',
                            url:'/index/forumList'

						},
					]
				},
			]
        }
    },
    getProjectName(){
        return {
            projectName: `花海摄影工作室`
        } 
    }
}
export default config
