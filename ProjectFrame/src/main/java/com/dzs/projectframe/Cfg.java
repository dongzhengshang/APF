package com.dzs.projectframe;

import com.dzs.projectframe.base.ProjectContext;

/**
 * 配置文件
 *
 * @author DZS dzsk@outlook.com
 * @version V1.0
 * @date 2016/8/19.
 */
public class Cfg {
	public static boolean IS_DEBUG = false;
	public static String APP_ROOT = ProjectContext.appContext.getPackageName();
	public static String SHARED_PREFER_APPLICATION = "ApplicationData";
	
	/*操作结果枚举类*/
	public enum OperationResultType {
		SUCCESS(""), FAIL(""),//成功/失败
		PROGRESS(""), FAIL_HAS_CACHE("");//进度/缓存
		private String message;
		
		OperationResultType(String message) {
			this.message = message;
		}
		
		public String getMessage() {
			return message;
		}
		
		public OperationResultType setMessage(String message) {
			this.message = message;
			return this;
		}
	}
	
	public interface OperationResult {
		void onResult(OperationResultType type);
	}
	
	/**
	 * 设置是否打印LOG
	 *
	 * @param isDebug 开关
	 */
	public static void setIsDebug(boolean isDebug) {
		IS_DEBUG = isDebug;
	}
}
