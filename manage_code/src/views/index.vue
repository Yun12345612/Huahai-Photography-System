<template>
  <div class="admin-shell" :class="{'is-collapsed': collapsed}">
    <aside class="admin-sidebar" aria-label="后台导航">
      <router-link class="admin-brand" to="/">
        <img src="/brand/huahai-logo.svg" alt="花海" />
        <span>花海摄影<small>STUDIO MANAGEMENT</small></span>
      </router-link>
      <el-scrollbar class="admin-nav-scroll">
        <el-menu ref="menuRef" class="studio-nav" :default-active="activePath" :default-openeds="opened"
          :collapse="collapsed" :collapse-transition="false" unique-opened router
          @open="openGroup" @close="closeGroup">
          <el-menu-item index="/"><el-icon><House /></el-icon><template #title>工作台</template></el-menu-item>
          <el-sub-menu v-for="group in navGroups" :key="group.id" :index="group.id"
            :class="{'has-active-page': group.id === activeGroup}" popper-class="studio-sidebar-popup">
            <template #title><el-icon><component :is="group.icon" /></el-icon><span>{{ group.label }}</span></template>
            <el-menu-item v-for="item in group.items" :key="item.path" :index="item.path">
              <el-icon><component :is="item.icon" /></el-icon><template #title>{{ item.label }}</template>
            </el-menu-item>
          </el-sub-menu>
        </el-menu>
      </el-scrollbar>

    </aside>
    <section class="admin-workspace">
      <header class="admin-header">
        <el-button text circle @click="collapsed = !collapsed" aria-label="展开或收起菜单" :aria-expanded="!collapsed">
          <el-icon><Fold v-if="!collapsed" /><Expand v-else /></el-icon>
        </el-button>
        <span>工作空间 / <strong>{{ route.meta.title || '工作台' }}</strong></span>
        <div>
          <MessageBadge token-key="Token" destination="/message-center" />
          <WorkspacePreferences :account="name" :role="role" />
          <a :href="frontUrl" target="_blank" rel="noopener">查看前台 ↗</a>
          <el-dropdown trigger="click"><button class="admin-account">{{ name }} · {{ role }} ⌄</button>
            <template #dropdown><el-dropdown-menu>
              <el-dropdown-item @click="router.push('/'+sessionTable+'Center')">个人中心</el-dropdown-item>
              <el-dropdown-item @click="router.push('/updatepassword')">修改密码</el-dropdown-item>
              <el-dropdown-item divided @click="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu></template>
          </el-dropdown>
        </div>
      </header>
      <main class="admin-content">
        <header v-if="route.path !== '/'" class="admin-page-heading"><span>HUAHAI / WORKSPACE</span><h1>{{ route.meta.title }}</h1></header>
        <router-view :key="route.fullPath" />
      </main>
    </section>
  </div>
</template>

<script setup>
import {computed, ref, watch, nextTick, onMounted} from 'vue'
import WorkspacePreferences from '@/components/WorkspacePreferences.vue'
import MessageBadge from '@/components/MessageBadge.vue'
import {useRoute, useRouter} from 'vue-router'
import {useStore} from 'vuex'

const route = useRoute(), router = useRouter(), store = useStore(), menuRef = ref(null)
const collapsed = ref(localStorage.getItem('huahai-sidebar') === null ? window.innerWidth < 900 : localStorage.getItem('huahai-sidebar') === 'true')
const role = localStorage.getItem('role') || ''
const sessionTable = localStorage.getItem('sessionTable') || 'users'
const name = localStorage.getItem('adminName') || role
const groupStorageKey = 'huahai-sidebar-group-' + sessionTable
const savedGroup = localStorage.getItem(groupStorageKey)
const opened = ref(savedGroup === null ? ['booking'] : savedGroup ? [savedGroup] : [])
const frontUrl = new URL(window.location.href)
frontUrl.port = '8083'
frontUrl.hash = '/index/home'

const groupDefinitions = [
  {id:'booking', label:'预约与订单', icon:'Calendar', tables:['sheyingshiyuyue', 'booking-calendar', 'booking-changes', 'booking-refunds']},
  {id:'content', label:'摄影内容', icon:'Picture', tables:['sheyingshixinxi', 'sheyingzuopin', 'fenlei']},
  {id:'activities', label:'活动管理', icon:'Present', tables:['huodongxinxi', 'baomingxinxi']},
  {id:'communication', label:'消息与互动', icon:'ChatDotRound', tables:['message-center', 'chat', 'forum', 'discusssheyingshixinxi']},
  {id:'people', label:'人员管理', icon:'User', tables:['yonghu', 'sheyingshi', 'users']},
  {id:'system', label:'系统管理', icon:'Setting', tables:['config', 'friendshipLink', 'operation-logs']}
]
const labels = {sheyingshiyuyue:'拍摄预约', fenlei:'作品分类', huodongxinxi:'活动发布', baomingxinxi:'活动报名', yonghu:'客户账号', sheyingshi:'摄影师账号', users:'管理员账号', config:'轮播图设置'}
const icons = {sheyingshiyuyue:'Calendar', 'booking-calendar':'Clock', 'booking-changes':'Refresh', 'booking-refunds':'Wallet',
  sheyingshixinxi:'Camera', sheyingzuopin:'Picture', fenlei:'Collection', huodongxinxi:'Present', baomingxinxi:'Tickets',
  'message-center':'Bell', chat:'Service', forum:'ChatLineSquare', discusssheyingshixinxi:'Comment', yonghu:'User',
  sheyingshi:'Avatar', users:'UserFilled', config:'PictureFilled', friendshipLink:'Link', 'operation-logs':'DocumentChecked'}

function menuPath(child) {
  let table = child.classname || child.tableName
  if(table === 'center') table = sessionTable + 'Center'
  return '/' + table + (child.menuJump ? '?menuJump=' + encodeURIComponent(child.menuJump) : '')
}
// Keep the saved role menu as the authority; grouping only changes navigation order.
const navGroups = computed(() => {
  let source = []
  try {
    const menus = JSON.parse(localStorage.getItem('menus') || '[]')
    const current = menus.find(item => item.tableName === sessionTable) || menus.find(item => item.roleName === role)
    source = (current?.backMenu || []).flatMap(group => group.child || [])
  } catch { source = [] }
  const entries = source.map(child => {
    const table = child.classname || child.tableName
    return {table, path:menuPath(child), label:labels[table] || child.menu, icon:icons[table] || 'Document'}
  })
  if(['users','sheyingshi'].includes(sessionTable)) {
    if(entries.some(item => item.table === 'sheyingshiyuyue')) {
      entries.push({table:'booking-calendar', path:'/booking-calendar', label:'档期日历', icon:icons['booking-calendar']})
      entries.push({table:'booking-changes', path:'/booking-changes', label:'改期与取消', icon:icons['booking-changes']})
      entries.push({table:'booking-refunds', path:'/booking-refunds', label:'退款管理', icon:icons['booking-refunds']})
    }
    entries.push({table:'message-center', path:'/message-center', label:'消息中心', icon:'Bell'})
  }
  if(sessionTable === 'users') entries.push({table:'operation-logs', path:'/operation-logs', label:'操作日志', icon:'DocumentChecked'})
  const unique = entries.filter((entry,index,all) => all.findIndex(item => item.path === entry.path) === index)
  const result = groupDefinitions.map(group => ({...group, items:group.tables.flatMap(table => unique.filter(item => item.table === table))})).filter(group => group.items.length)
  const known = new Set(groupDefinitions.flatMap(group => group.tables))
  const other = unique.filter(item => !known.has(item.table))
  if(other.length) result.push({id:'other', label:'其他功能', icon:'More', items:other})
  return result
})
const activeItem = computed(() => navGroups.value.flatMap(group => group.items).find(item => item.path === route.fullPath)
  || navGroups.value.flatMap(group => group.items).find(item => item.path.split('?')[0] === route.path))
const activePath = computed(() => activeItem.value?.path || route.path)
const activeGroup = computed(() => navGroups.value.find(group => group.items.some(item => item.path === activeItem.value?.path))?.id || '')

function openGroup(id) {
  if(collapsed.value) return
  opened.value = [id]
  localStorage.setItem(groupStorageKey, id)
}
function closeGroup(id) {
  if(collapsed.value) return
  if(opened.value.includes(id)) { opened.value = []; localStorage.setItem(groupStorageKey, '') }
}
async function revealCurrentGroup() {
  if(collapsed.value) return
  const id = activeGroup.value || opened.value[0]
  await nextTick()
  if(id && navGroups.value.some(group => group.id === id)) menuRef.value?.open(id)
}
watch(collapsed, async value => {
  localStorage.setItem('huahai-sidebar', String(value))
  if(!value) await revealCurrentGroup()
})
watch(() => route.fullPath, revealCurrentGroup)
onMounted(revealCurrentGroup)
function logout() {
  for(const key of ['Token','role','sessionTable','adminName','back_session','admin_userid']) localStorage.removeItem(key)
  store.dispatch('user/loginOut')
  router.replace('/login')
}
if(!localStorage.getItem('Token')) router.replace('/login')
else store.dispatch('user/getSession')
</script>

<style scoped>
.admin-sidebar {transition:width .2s ease; overflow:hidden}
.admin-workspace {transition:margin-left .2s ease, width .2s ease}
.admin-brand {flex-shrink:0; white-space:nowrap}
.studio-nav {padding:6px 0 12px; background:transparent; --el-menu-bg-color:transparent; --el-menu-text-color:var(--el-text-color-regular); --el-menu-hover-bg-color:var(--el-fill-color-light)}
.studio-nav:not(.el-menu--collapse) {width:223px}
.studio-nav :deep(.el-sub-menu__title) {height:46px; margin:5px 12px; padding-left:16px !important; border-radius:8px; font-weight:500}
.studio-nav :deep(.el-sub-menu__title .el-icon), .studio-nav :deep(.el-menu-item .el-icon) {font-size:18px; margin-right:10px}
.studio-nav :deep(.el-menu-item) {height:42px; line-height:42px; margin:4px 12px; border-radius:8px}
.studio-nav > :deep(.el-menu-item) {padding-left:16px !important}
.studio-nav :deep(.el-sub-menu .el-menu-item) {min-width:0; padding-left:44px !important; font-size:13px; color:var(--el-text-color-regular)}
.studio-nav :deep(.el-sub-menu .el-menu-item .el-icon) {font-size:15px; width:18px; margin-right:8px; color:var(--el-text-color-secondary)}
.studio-nav :deep(.el-menu-item.is-active) {background:var(--el-color-primary-light-9); color:var(--el-color-primary)}
.studio-nav :deep(.el-menu-item.is-active .el-icon) {color:var(--el-color-primary)}
.studio-nav :deep(.has-active-page>.el-sub-menu__title) {color:var(--el-color-primary)}
.is-collapsed .studio-nav {width:64px}
.is-collapsed .studio-nav :deep(.el-sub-menu__title), .is-collapsed .studio-nav :deep(.el-menu-item) {margin:5px 8px; padding:0 12px !important; border-radius:8px}
.is-collapsed .studio-nav :deep(.el-sub-menu__title .el-icon), .is-collapsed .studio-nav :deep(.el-menu-item .el-icon) {margin-right:0}
.is-collapsed .studio-nav > :deep(.el-menu-item) {width:48px}
.is-collapsed .studio-nav :deep(.el-menu-item > .el-tooltip__trigger) {width:48px; padding:0 12px !important}
@media(max-width:760px) {
  .admin-shell:not(.is-collapsed) .admin-sidebar {width:224px}
  .admin-shell:not(.is-collapsed) .admin-workspace {margin-left:224px; width:calc(100% - 224px)}
}
@media(prefers-reduced-motion:reduce) {.admin-sidebar,.admin-workspace {transition:none}}
</style>
<style>
.studio-sidebar-popup .el-menu {min-width:190px; padding:6px}
.studio-sidebar-popup .el-menu-item {height:40px; line-height:40px; margin:3px 0; border-radius:6px; font-size:13px}
.studio-sidebar-popup .el-menu-item.is-active {background:var(--el-color-primary-light-9)}
</style>
