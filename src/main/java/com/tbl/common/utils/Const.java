package com.tbl.common.utils;

/**
 * 常用常量
 *
*/
public class Const {
	public static final String SESSION_USER = "sessionUser";

	/**
	 * 定时任务状态
	 */
	public enum ScheduleStatus {
		/**
		 * 正常
		 */
		NORMAL(0),
		/**
		 * 暂停
		 */
		PAUSE(1);

		private int value;

		ScheduleStatus(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}

}
