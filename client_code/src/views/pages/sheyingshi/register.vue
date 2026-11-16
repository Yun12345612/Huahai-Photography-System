<template>
  <main class="photographer-register">
    <aside class="register-story">
      <router-link to="/index/home" class="story-brand">花海摄影工作室 <small>HUAHAI PHOTO STUDIO</small></router-link>
      <div class="story-copy"><span>PHOTOGRAPHER APPLICATION</span><h1>让你的镜头<br>被更多人看见。</h1><p>加入花海摄影工作室，和客户一起记录值得珍藏的瞬间。</p></div>
      <div class="story-steps"><div class="active"><i>01</i><span>提交资料</span></div><b></b><div><i>02</i><span>工作室审核</span></div><b></b><div><i>03</i><span>登录工作台</span></div></div>
    </aside>
    <section class="register-panel">
      <a class="back" :href="staffLoginUrl">← 返回工作人员登录</a>
      <div class="register-content">
        <span class="kicker">JOIN THE STUDIO</span><h2>申请成为摄影师</h2><p class="subtitle">填写基本资料，审核通过后即可使用摄影师工作台。</p>
        <el-form :model="form" class="application-form" label-position="top" @submit.prevent="submit">
          <div class="form-grid">
            <el-form-item label="摄影账号" class="span-two"><el-input v-model="form.sheyingzhanghao" autocomplete="username" placeholder="设置用于工作人员登录的账号" maxlength="50" /></el-form-item>
            <el-form-item label="登录密码"><el-input v-model="form.mima" type="password" show-password autocomplete="new-password" placeholder="至少设置一个安全密码" /></el-form-item>
            <el-form-item label="确认密码"><el-input v-model="form.mima2" type="password" show-password autocomplete="new-password" placeholder="再次输入登录密码" /></el-form-item>
            <el-form-item label="摄影师姓名"><el-input v-model="form.sheyingxingming" placeholder="用于工作室联系与资料展示" maxlength="80" /></el-form-item>
            <el-form-item label="联系电话"><el-input v-model="form.lianxidianhua" maxlength="11" placeholder="请输入常用手机号" /></el-form-item>
          </div>
          <el-alert class="review-notice" title="提交后进入待审核状态，通过后可使用工作人员入口登录。" type="info" :closable="false" />
          <el-button class="submit" native-type="submit" :loading="loading">提交审核申请 <span>→</span></el-button>
        </el-form>
        <p class="login-hint">已有摄影师账号？<a :href="staffLoginUrl">返回登录</a></p>
      </div>
      <small class="copyright">HUAHAI PHOTO STUDIO · 留住此刻的美好</small>
    </section>
  </main>
</template>
<script setup>
import {ref,getCurrentInstance} from 'vue'
const context=getCurrentInstance()?.appContext.config.globalProperties
const staffLoginUrl = window.location.protocol + "//" + window.location.hostname + ":8082/#/login";
const loading=ref(false),form=ref({sheyingzhanghao:'',mima:'',mima2:'',sheyingxingming:'',lianxidianhua:''})
const submit=async()=>{if(!form.value.sheyingzhanghao||!form.value.mima||!form.value.sheyingxingming)return context?.$toolUtil.message('请填写账号、密码和姓名','warning');if(form.value.mima!==form.value.mima2)return context?.$toolUtil.message('两次密码输入不一致','warning');if(form.value.lianxidianhua&&!context?.$toolUtil.isMobile(form.value.lianxidianhua))return context?.$toolUtil.message('请输入正确的手机号','warning');loading.value=true;try{await context.$http.post('sheyingshi/register',{sheyingzhanghao:form.value.sheyingzhanghao,mima:form.value.mima,sheyingxingming:form.value.sheyingxingming,lianxidianhua:form.value.lianxidianhua});context.$toolUtil.message('申请已提交，请等待管理员审核','success',()=>window.location.href='http://localhost:8082/#/login')}finally{loading.value=false}}
</script>
<style scoped>.photographer-register{min-height:100vh;display:grid;grid-template-columns:1fr 1fr;background:#faf9f6;color:#294735}.register-story{padding:48px;background:linear-gradient(145deg,#365e4b,#172f27);color:#fff;display:flex;flex-direction:column;justify-content:space-between}.register-story a{color:#fff;text-decoration:none;font-size:23px;letter-spacing:3px}.register-story small{font-size:10px;letter-spacing:3px;color:#cbd9c4}.register-story h1{font-family:SimSun,serif;font-size:clamp(34px,4vw,56px);font-weight:400;line-height:1.5;letter-spacing:4px}.register-story p{font-size:13px;color:#cfddca}.register-panel{position:relative;display:flex;align-items:center;justify-content:center;padding:70px 40px}.back{position:absolute;top:38px;left:40px;color:#758474;text-decoration:none;font-size:13px}.register-card{width:100%;max-width:420px}.kicker{font-size:10px;letter-spacing:3px;color:#8b9988}.register-card h2{font-size:32px;font-weight:500;margin:14px 0 10px}.subtitle,.notice{font-size:13px;line-height:1.8;color:#7d8b7d}.subtitle{margin-bottom:28px}.register-card :deep(.el-form-item){margin-bottom:18px}.register-card :deep(.el-input__wrapper){min-height:44px;background:#fff}.submit{width:100%;height:46px;margin-top:8px;background:#305d4c;border-color:#305d4c;color:#fff}.notice{margin-top:22px;border-top:1px solid #e3e9e0;padding-top:18px}@media(max-width:760px){.photographer-register{display:block}.register-story{display:none}.register-panel{min-height:100vh;padding:90px 24px 36px}.back{left:24px}}
</style>