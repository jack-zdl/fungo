package com.game.common.utils;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * <p>
 * 			PK值生成工具
 * </p>
 *
 * @author mxf
 * @since 2018-12-04
 */
public class PKUtil {

	// PK生成锁,用来限定同一时刻只有一个线程进入PK生成计算
	private final Lock LOCK = new ReentrantLock();

	// 初始化时的毫秒数,因为该时间会随系统时间的改变而改变, 所以计算方法为该时间加上通过 nanoTime 计算出来的时间差
	private final static Long startMilli = System.currentTimeMillis();

	// 初始化时的纳秒数,用来计量时间差,nanoTime不会随着系统时间的修改而改变
	private final static long startNano = System.nanoTime();

	// 记录上一次生成 的 PK,如果新生成的PK和上次相等,则需要再次生成
	// 每次被线程访问时,都强迫从共享内存中重读该成员变量的值.而且,当成员变量发生变化时,强迫线程将变化值回写到共享内存
	private volatile long lastPK = -1;

	// 时间格式
	private final static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");

	private static int suffix = 0;

	private static final Map<Integer, PKUtil> instanceMap = new HashMap<Integer, PKUtil>();

	// 必须提供正确的参数,以保证 suffix 在集群环境的唯一性
	private PKUtil(final int suffix) {
		PKUtil.suffix = suffix;
	}

	/**
	 * 获得主键生成器实例,校验位从系统配置文件中读出.
	 *
	 * @return 主键生成器实例
	 */
	public synchronized static PKUtil getInstance() {
		return getInstance(suffix);
	}

	/**
	 * 获得主键生成器实例,校验位通过参数设置 单机环境下,应该保证用相同的 suffix 在集群环境中,不同的机器必须提供不同的 suffix 来保证生成的ID的唯一性
	 *
	 * @param suffix
	 *            唯一标识
	 * @return 主键生成器实例
	 */
	public synchronized static PKUtil getInstance(int suffix) {
		PKUtil pkgen;
		if (!instanceMap.containsKey(suffix)) {
			pkgen = new PKUtil(suffix);
			instanceMap.put(suffix, pkgen);
		} else {
			pkgen = instanceMap.get(suffix);
		}
		return pkgen;
	}

	/**
	 * 返回下一个 long 型 PK, format: 2007101011023022291 <br>
	 * yyyyMMddHHmmssSSS + Micro Seconds + suffix
	 *
	 * @return long PK
	 */
	public long longPK() {
		LOCK.lock();
		try {
			long newPK;
			do {
				long pastNano = System.nanoTime() - startNano; // 纳秒时间差
				long milliTime = pastNano / 1000000; // 取得毫秒差
				long microTime = (pastNano / 100000) % 10; // 取得微秒第一位,
				// 计算出来的long PK,精度到万分之一秒（百微妙）,加上尾数,一共19位,这是 Long.MAX_VALUE的最大位数了
				SecureRandom sr = new SecureRandom();
				int randomNum = Math.abs(sr.nextInt() % 10);
				suffix = suffix == 0 ? randomNum : suffix;
				newPK = Long.parseLong(dateFormat.format(new Date(startMilli + milliTime)) + microTime + suffix);
			} while (lastPK == newPK); // 如果生成的相同,则再次计算
			lastPK = newPK; // 设置 lastPK
			return newPK;
		} finally {
			LOCK.unlock();
		}
	}

	public static void main(String[] args) {
		for (int i=0;i<20;i++) {
			System.out.println(PKUtil.getInstance().longPK());
		}
	}

}
