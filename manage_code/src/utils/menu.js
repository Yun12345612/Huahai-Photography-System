import toolUtil from '@/utils/toolUtil'
const menu = {
	list() {
		if(toolUtil.storageGet("menus")) {
			//则使用 eval 函数将字符串形式的数据解析为 JavaScript 对象并返回
		    return eval('(' + toolUtil.storageGet("menus")+ ')');
		} else {
		    return null;
		}
	}
}
export default menu;