import {
	ElMessage,
	ElMessageBox,
	ElNotification
} from 'element-plus'
import menu from './menu';
import CryptoJS from './cryptojs.js'
let KEY = '1234567890123456'
let IV = 'abcdefghijklmnop'
const toolUtil = {
	//提示语
	message(msg, type, close = null) {
		ElMessage({
			message: msg,
			type: type,
			duration: 2500,
			onClose() {
				if (close) {
					close()
				}
			}
		})
	},
	//右部提示语
	notify(title, msg, type = 'success', close = null) {
		ElNotification({
			title: title,
			message: msg,
			type: type,
			onClose() {
				if (close) {
					close()
				}
			}
		})
	},
	storageSet(key, value) {
		localStorage.setItem(key, value);
	},
	storageGet(key) {
		return localStorage.getItem(key) ? localStorage.getItem(key) : "";
	},
	storageGetObj(key) {
		return localStorage.getItem(key) ? JSON.parse(localStorage.getItem(key)) : null;
	},
	storageRemove(key) {
		localStorage.removeItem(key);
	},
	storageClear() {
		localStorage.removeItem('Token');
		localStorage.removeItem('role');
		localStorage.removeItem('sessionTable');
		localStorage.removeItem('adminName');
	},
	/**
	 * 邮箱
	 * 
	 */
	isEmail(s) {
		return /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((.[a-zA-Z0-9_-]{2,3}){1,2})$/.test(s)
	},
	/**
	 * 手机号码
	 * 
	 */
	isMobile(s) {
		return /^(0|86|17951)?(13[0-9]|15[012356789]|16[6]|19[89]|17[01345678]|18[0-9]|14[579]|19[0-35-9])[0-9]{8}$/.test(s)
	},
	/**
	 * 电话号码
	 * 
	 */
	isPhone(s) {
		return /^([0-9]{3,4}-)?[0-9]{7,8}$/.test(s)
	},
	/**
	 * URL地址
	 * 
	 */
	isURL(s) {
		return /^http[s]?:\/\/.*/.test(s)
	},
	/**
	 * 匹配数字，可以是小数，不可以是负数,可以为空
	 *  
	 */
	isNumber(s) {
		return /(^-?[+-]?([0-9]*\.?[0-9]+|[0-9]+\.?[0-9]*)([eE][+-]?[0-9]+)?$)|(^$)/.test(s);
	},
	/**
	 * 匹配整数，可以为空
	 *  
	 */
	isIntNumer(s) {
		return /(^-?\d+$)|(^$)/.test(s);
	},
	/**
	 * 身份证校验
	 */
	checkIdCard(idcard) {
		const regIdCard = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
		if (!regIdCard.test(idcard)) {
			return false;
		} else {
			return true;
		}
	},
	//form表单验证
	validator:{
		//匹配整数
		intNumber(rule, value, callback){
			if (!value) {
				callback();
			} else if (!toolUtil.isIntNumer(value)) {
				callback(new Error("请输入整数"));
			} else {
				callback();
			}
		},
		//匹配数字(整数/浮点数)
		number(rule, value, callback){
			if(!value){
				callback();
			} else if (!toolUtil.isNumber(value)) {
				callback(new Error("请输入数字"));
			} else {
				callback();
			}
		},
		//匹配手机号码
		mobile(rule, value, callback){
			if(!value){
				callback();
			} else if (!toolUtil.isMobile(value)) {
				callback(new Error("请输入正确的手机号码"));
			} else {
				callback();
			}
		},
		//匹配电话号码
		phone(rule, value, callback){
			if(!value){
				callback();
			} else if (!toolUtil.isPhone(value)) {
				callback(new Error("请输入正确的电话号码"));
			} else {
				callback();
			}
		},
		//匹配邮箱
		email(rule, value, callback){
			if(!value){
				callback();
			} else if (!toolUtil.isEmail(value)) {
				callback(new Error("请输入正确的邮箱地址"));
			} else {
				callback();
			}
		},
		//匹配身份证
		idCard(rule, value, callback){
			if(!value){
				callback();
			} else if (!toolUtil.checkIdCard(value)) {
				callback(new Error("请输入正确的身份证号码"));
			} else {
				callback();
			}
		},
		//匹配网站地址
		url(rule, value, callback){
			if(!value){
				callback();
			} else if (!toolUtil.isURL(value)) {
				callback(new Error("请输入正确的URL地址"));
			} else {
				callback();
			}
		},
	},
	/**
	 * 是否有权限
	 * @param {*} key
	 */
	isAuth(tableName, key) {
		// 从本地存储中获取当前用户的角色信息
		let role = toolUtil.storageGet("role");
		// 如果没有获取到角色信息，则默认角色为“管理员”
		if (!role) {
			role = '管理员';
		}
		// 获取菜单项列表，用于判断当前角色的权限配置
		let menus = menu.list();
		// 如果存在菜单项列表且列表不为空
		if (menus && menus.length) {
			// 遍历菜单项列表，寻找与当前角色匹配的菜单项
			for (let i = 0; i < menus.length; i++) {
				// 如果当前菜单项的角色名称与当前角色匹配
				if (menus[i].roleName == role) {
					// 如果匹配的菜单项有后台菜单配置
					if (menus[i].backMenu && menus[i].backMenu.length) {
						// 初始化权限标记为false，表示未找到对应权限
						let flag = false;
						// 遍历后台菜单配置
						for (let j = 0; j < menus[i].backMenu.length; j++) {
							// 如果后台菜单配置中有子菜单项
							if (menus[i].backMenu[j].child && menus[i].backMenu[j].child.length) {
								// 遍历子菜单项，寻找与当前资源匹配的配置
								for (let k = 0; k < menus[i].backMenu[j].child.length; k++) {
									// 如果资源名称以斜杠分隔（比如“资源/子资源”格式）
									if (tableName.split('/').length > 1) {
										// 判断当前子菜单项的资源名称和跳转路径是否与传入的资源名称匹配
										if (tableName.split('/')[0] == menus[i].backMenu[j].child[k].tableName
											&& tableName.split('/')[1] == menus[i].backMenu[j].child[k].menuJump) {
											// 如果匹配成功，则判断当前操作是否在该子菜单项的按钮权限中
											flag = menus[i].backMenu[j].child[k].buttons.includes(key);
										}
									} else {
										// 如果资源名称不是以斜杠分隔的格式
										if (tableName == 'orders') {
											// 如果是“orders”资源，且没有跳转路径配置，则仅判断资源名称是否匹配
											if (!menus[i].backMenu[j].child[k].menuJump
												&& tableName == menus[i].backMenu[j].child[k].tableName) {
												// 判断当前操作是否在该子菜单项的按钮权限中
												flag = menus[i].backMenu[j].child[k].buttons.includes(key);
											}
										} else {
											// 其他情况下，仅判断资源名称是否匹配
											if (tableName == menus[i].backMenu[j].child[k].tableName) {
												// 判断当前操作是否在该子菜单项的按钮权限中
												flag = menus[i].backMenu[j].child[k].buttons.includes(key);
											}
										}
									}
									// 如果找到了匹配的权限，则立即返回结果
									if (flag) {
										return flag;
									}
								}
							}
						}
					}
				}
			}
		}
		// 如果未找到匹配的权限，则返回false
		return false;
	},
	/**
	 * 获取当前时间（yyyy-MM-dd hh:mm:ss）
	 */
	getCurDateTime() {
		let currentTime = new Date(),
			year = currentTime.getFullYear(),
			month = currentTime.getMonth() + 1 < 10 ? '0' + (currentTime.getMonth() + 1) : currentTime.getMonth() +
			1,
			day = currentTime.getDate() < 10 ? '0' + currentTime.getDate() : currentTime.getDate(),
			hour = currentTime.getHours()<10 ? '0' + currentTime.getHours():currentTime.getHours(),
			minute = currentTime.getMinutes()<10? '0' + currentTime.getMinutes():currentTime.getMinutes(),
			second = currentTime.getSeconds()<10? '0' + currentTime.getSeconds():currentTime.getSeconds();
		return year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
	},
	/**
	 *  获取当前日期（yyyy-MM-dd）
	 */
	getCurDate() {
		let currentTime = new Date(),
			year = currentTime.getFullYear(),
			month = currentTime.getMonth() + 1 < 10 ? '0' + (currentTime.getMonth() + 1) : currentTime.getMonth() +
			1,
			day = currentTime.getDate() < 10 ? '0' + currentTime.getDate() : currentTime.getDate();
		return year + "-" + month + "-" + day;
	},
	encryptDes(message) {
		const keyHex = CryptoJS.enc.Utf8.parse(KEY);
		const encrypted = CryptoJS.DES.encrypt(message, keyHex, {
			mode: CryptoJS.mode.ECB,
			padding: CryptoJS.pad.Pkcs7
		});
		return encrypted.toString();
	},
	decryptDes(ciphertext) {
		const keyHex = CryptoJS.enc.Utf8.parse(KEY);
		// direct decrypt ciphertext
		const decrypted = CryptoJS.DES.decrypt({
			ciphertext: CryptoJS.enc.Base64.parse(ciphertext)
		}, keyHex, {
			mode: CryptoJS.mode.ECB,
			padding: CryptoJS.pad.Pkcs7
		});
		return decrypted.toString(CryptoJS.enc.Utf8);
	},
	encryptAes(msg) {
		let cmode = CryptoJS.mode.CBC;
		let cpad = CryptoJS.pad.Pkcs7;
		let ciphertext = CryptoJS.AES.encrypt(msg, CryptoJS.enc.Utf8.parse(KEY), {
			mode: cmode,
			padding: cpad,
			iv: CryptoJS.enc.Utf8.parse(IV)
		}).toString();
		return ciphertext;
	},
	decryptAes(msg) {
		let cmode = CryptoJS.mode.CBC;
		let cpad = CryptoJS.pad.Pkcs7;
		var bytes = CryptoJS.AES.decrypt(msg, CryptoJS.enc.Utf8.parse(KEY), {
			mode: cmode,
			padding: cpad,
			iv: CryptoJS.enc.Utf8.parse(IV)
		});
		var originText = bytes.toString(CryptoJS.enc.Utf8);
		return originText;
	}
}
export default toolUtil;
