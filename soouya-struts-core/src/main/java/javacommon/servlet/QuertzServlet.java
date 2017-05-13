package javacommon.servlet;

import com.soouya.common.util.ErrorUtil;
import com.soouya.common.util.IpUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Scheduler;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.util.ResourceBundle;


public class QuertzServlet extends HttpServlet {

	private static final long serialVersionUID = -6493723009857248303L;
	protected static Log log = LogFactory.getLog(QuertzServlet.class);

	//private Scheduler scheduler = QuartzScheduleMgr.getInstanceScheduler();
	
 	private void generateExcel() {
/* 		try {
			JobDetail jobDetail = JobBuilder.newJob(GenerateExcelJob.class).withIdentity("generateExcelDetail", "generateExcelGroup").build();

			String trigger="0 1 * * * ?";

			CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity("generateExcelTrigger", "generateExcelTriggerGroup").withSchedule(CronScheduleBuilder.cronSchedule(trigger)).build(); // 在任务调度器中，使用任务调度器的
			scheduler.scheduleJob(jobDetail, cronTrigger);
			scheduler.start();
			log.info("generateExcel run");
		} catch (Exception e) {
			ErrorUtil.errorHandle(e);
		}*/
	}

	private void generateRedWoodExcel() {
		try {
			//JobDetail jobDetail = JobBuilder.newJob(GenerateExcelForRedWoodJob.class).withIdentity("generateExcelDetail", "generateExcelGroup").build();

			String trigger="0 0 1 * * ?";
//			String trigger="0 */1 * * * ?";

			CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity("generateExcelTrigger", "generateExcelTriggerGroup").withSchedule(CronScheduleBuilder.cronSchedule(trigger)).build(); // 在任务调度器中，使用任务调度器的
/*			scheduler.scheduleJob(jobDetail, cronTrigger);
			scheduler.start();*/
			log.info("generateExcel run");
		} catch (Exception e) {
			ErrorUtil.errorHandle(e);
		}
	}

	/**
	 * 正常报表数据
	 */
	private void statisticsJobScheduler() {
/*		IStatisticsManager statisticsManager = SpringContextHolder.getBean("statisticsManager");
		StatisticsQuery statisticsQuery = new StatisticsQuery();
		statisticsQuery.setPageSize(1000);
		statisticsQuery.setSortColumns("create_time desc");
		//statisticsQuery.setType("statistics_for_kf");
		try {
			Page<Statistics> statisticsPage = statisticsManager.findPage(statisticsQuery);
			List<Statistics> statisticsList = statisticsPage.getResult();
			for (Object o : statisticsList) {
				Statistics statistics = (Statistics) o;
				//生成JobDetail
				JobBuilder jobBuilder = JobBuilder.newJob(StatisticsJob.class);
				JobDetail jobDetail = jobBuilder.withIdentity(QuartzScheduleMgr.getJobKey(statistics)).build();
				//生成CronTrigger
				CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(QuartzScheduleMgr.getTriggerKey(statistics))
						.withSchedule(CronScheduleBuilder.cronSchedule(statistics.getSchedule())).build();
				scheduler.scheduleJob(jobDetail, cronTrigger);
				scheduler.start();
			}
			log.info("statisticsJobScheduler run");
		} catch (Exception e) {
			ErrorUtil.errorHandle(e);
		}*/
	}

	/**
	 * 生成历史报表数据
	 */
	private void historyStatisticsJobScheduler() {
/*		IStatisticsManager statisticsManager = SpringContextHolder.getBean("statisticsManager");
		StatisticsQuery statisticsQuery = new StatisticsQuery();
		statisticsQuery.setPageSize(100);
		statisticsQuery.setSortColumns("create_time desc");
		//按照类型执行
		statisticsQuery.setType("statistics_for_kf");
		try {
			Page<Statistics> statisticsPage = statisticsManager.findPage(statisticsQuery);
			List<Statistics> statisticsList = statisticsPage.getResult();
			for (Object o : statisticsList) {
				Statistics statistics = (Statistics) o;
				//生成JobDetail
				JobBuilder jobBuilder = JobBuilder.newJob(HistoryStatisticsJob.class);
				JobDetail jobDetail = jobBuilder.withIdentity(QuartzScheduleMgr.getJobKey(statistics)).build();
				//生成CronTrigger
				CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(QuartzScheduleMgr.getTriggerKey(statistics))
						.withSchedule(CronScheduleBuilder.cronSchedule(statistics.getSchedule())).build();
				scheduler.scheduleJob(jobDetail, cronTrigger);
				scheduler.start();
			}
			log.info("statisticsJobScheduler run");
		} catch (Exception e) {
			ErrorUtil.errorHandle(e);
		}*/
	}

	@Override
	public void init() throws ServletException {
		System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");//Comparison method violates its general contract
		// 读取配置
		ResourceBundle resourceBundle = ResourceBundle.getBundle("application");// application.properties
		final String quartz_IP = resourceBundle.getString("quartzTask.host");
		final String quartzTaskOpenFlag = resourceBundle.getString("quartzTask.openFlag");// 系统接口打开标记
		final String openFlag = resourceBundle.getString("quartzTask.userInfoManager.isTodayBuy.openFlag");
		String ip= IpUtil.getLocalIP();
		if(quartz_IP==null){
			log.error("quartz_IP is null, quartz not run.");
			return;
		}
		if(ip==null){
			log.error("ip is null, quartz not run.");
			return;
		}
		log.info("ip:"+ip+",quartzIP:"+quartz_IP);
		if(ip.contains(quartz_IP)){
			log.info("quartz start...");
			//randClothJobScheduler();
			//正常数据
			statisticsJobScheduler();
			//历史数据
			//historyStatisticsJobScheduler();
			generateRedWoodExcel();
			//pushJobScheduler();
			//updateTagJobScheduler();
		}else{
			log.warn("quartz not start");
		}
	}

	@Override
	public void destroy() {
		try {
			Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
			scheduler.shutdown(true);
			log.info("scheduler destrory");
		} catch (Exception e) {
			ErrorUtil.errorHandle(e);
		}
		
		super.destroy();
	}
}
