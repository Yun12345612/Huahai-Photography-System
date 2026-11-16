<template>
  <main class="studio-login">
    <section class="login-story"><img v-if="cover" :src="cover" alt="花海摄影作品" /><div class="login-story-shade"></div><router-link class="login-brand" to="/index/home">花海摄影工作室 <span>HUAHAI PHOTO STUDIO</span></router-link><div class="login-story-copy"><span>EVERY MOMENT TELLS A STORY</span><h1>相遇光影，<br />记录属于你的故事。</h1><p>浏览心动作品，与摄影师沟通，预约下一次美好。</p></div></section>
    <section class="login-panel"><router-link class="back-home" to="/index/home">← 返回首页</router-link><div class="login-card"><span class="login-kicker">WELCOME BACK</span><h2>欢迎回来</h2><p class="login-subtitle">登录花海，开启你的专属拍摄之旅。</p><el-form ref="formRef" :model="form" :rules="rules" label-position="top" @submit.prevent="submit"><el-form-item label="用户账号" prop="username"><el-input v-model="form.username" placeholder="请输入用户账号" autocomplete="username" maxlength="100" size="large" clearable /></el-form-item><el-form-item label="登录密码" prop="password"><el-input v-model="form.password" placeholder="请输入密码" type="password" autocomplete="current-password" show-password size="large" /></el-form-item><div class="login-options"><el-checkbox v-model="remember">记住账号</el-checkbox></div><el-alert v-if="error" :title="error" type="error" :closable="false" show-icon class="login-error" /><el-button class="login-submit" native-type="submit" :loading="submitting">{{ submitting ? '正在登录…' : '登录' }}</el-button></el-form><div class="login-register">还没有账号？<router-link to="/yonghuRegister">立即注册 →</router-link></div><div class="login-hint">登录后可在线咨询、预约摄影师及查看预约进度。</div></div><small class="login-copyright">HUAHAI PHOTO STUDIO · 留住此刻的美好</small></section>
  </main>
</template>
<script setup>
import { ref, getCurrentInstance, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useStore } from 'vuex'
const context = getCurrentInstance().appContext.config.globalProperties
const router = useRouter(), store = useStore()
const formRef = ref(), submitting = ref(false), error = ref(''), remember = ref(false), cover = ref('')
const form = ref({ username: localStorage.getItem('frontRememberedAccount') || '', password: '' })
remember.value = !!form.value.username
localStorage.removeItem('frontLoginForm')
const rules = { username: [{ required:true, message:'请输入用户账号', trigger:'blur' }], password:[{ required:true, message:'请输入密码', trigger:'blur' }] }
async function submit() {
 if (submitting.value) return
 error.value = ''
 try { await formRef.value.validate() } catch { return }
 submitting.value = true
 try {
  const result = await context.$http.post('yonghu/login', new URLSearchParams({ username:form.value.username.trim(),password:form.value.password }), { headers:{ 'Content-Type':'application/x-www-form-urlencoded' } })
  localStorage.setItem('frontToken',result.data.token); localStorage.setItem('frontRole','用户'); localStorage.setItem('frontSessionTable','yonghu')
  if(remember.value) localStorage.setItem('frontRememberedAccount',form.value.username.trim()); else localStorage.removeItem('frontRememberedAccount')
  await store.dispatch('user/getSession')
  const destination = localStorage.getItem('toPath'); localStorage.removeItem('toPath')
  await router.replace(destination && destination.startsWith('/index/') ? destination : '/index/home')
 } catch (failure) { error.value = failure?.data?.msg || '登录失败，请检查账号密码或稍后重试。' }
 finally { submitting.value = false }
}
onMounted(async () => {
 try { const result = await context.$http.get('sheyingzuopin/list',{params:{page:1,limit:1,sort:'id',order:'desc'}}); const image = result.data.data.list[0]?.zuopintupian?.split(',')[0]; if(image) cover.value = /^https?:/.test(image) ? image : context.$config.url+image } catch {}
})
</script>
<style scoped>
.studio-login{--el-color-primary:#305d4c;min-height:100vh;display:grid;grid-template-columns:1.05fr 1fr;background:#faf9f6;color:#263e32;font-family:"Microsoft YaHei",sans-serif}.studio-login *{box-sizing:border-box}.login-story{position:relative;min-height:100vh;overflow:hidden;background:#294b3c}.login-story>img,.login-story-shade{position:absolute;inset:0;width:100%;height:100%;object-fit:cover}.login-story-shade{background:linear-gradient(180deg,#142c2580,#142c2530 30%,#142c25da)}.login-brand{position:absolute;top:44px;left:48px;color:white;text-decoration:none;font-size:23px;letter-spacing:3px}.login-brand span{display:block;margin-top:12px;font-size:10px;letter-spacing:4px;color:#e0e7dc}.login-story-copy{position:absolute;bottom:12%;left:48px;right:40px;color:#fff}.login-story-copy>span{font-size:10px;letter-spacing:3px;color:#d0dccb}.login-story-copy h1{font-family:SimSun,serif;font-size:clamp(30px,3vw,50px);font-weight:400;line-height:1.6;letter-spacing:4px;margin:20px 0}.login-story-copy p{font-size:13px;line-height:1.9;color:#d2dfd0}.login-panel{min-height:100vh;position:relative;display:flex;flex-direction:column;justify-content:center;padding:90px 40px}.back-home{position:absolute;top:44px;left:44px;text-decoration:none;color:#788373;font-size:13px}.login-card{width:100%;max-width:380px;margin:auto}.login-kicker{font-size:10px;letter-spacing:3px;color:#8a967f}.login-card h2{font-size:32px;font-weight:500;letter-spacing:3px;margin:16px 0 12px}.login-subtitle{font-size:13px;color:#7c8578;margin:0 0 36px;line-height:1.8}.login-card :deep(.el-input__wrapper){min-height:46px;border-radius:5px;background:#fff}.login-card :deep(.el-form-item){margin-bottom:24px}.login-card :deep(.el-form-item__label){font-size:13px;color:#4b5a45}.login-options{display:flex;align-items:center;justify-content:flex-start;gap:12px;margin:0 0 22px}.login-options a{font-size:12px;color:#617756;text-decoration:none}.login-options :deep(.el-checkbox__label){font-size:12px;color:#7c8578}.login-submit.el-button{width:100%;height:46px;margin:0;background:#305d4c;border:0;color:white;font-size:15px;letter-spacing:3px;border-radius:5px}.login-submit:hover{background:#244b3a}.login-register{display:flex;justify-content:center;gap:8px;margin-top:24px;font-size:13px;color:#8a9185}.login-register a{color:#305d4c;text-decoration:none}.login-hint{border-top:1px solid #e5e8de;margin-top:32px;padding-top:24px;font-size:11px;color:#92998c;line-height:1.8;text-align:center}.login-copyright{text-align:center;font-size:9px;letter-spacing:2px;color:#9aa290;margin-top:50px}.login-error{margin-bottom:20px}@media(max-width:800px){.studio-login{grid-template-columns:1fr}.login-story{display:none}.login-panel{padding:90px 24px 32px}.back-home{top:28px;left:24px}.login-card{max-width:400px}}@media(prefers-reduced-motion:reduce){*{transition:none!important}}
</style>
