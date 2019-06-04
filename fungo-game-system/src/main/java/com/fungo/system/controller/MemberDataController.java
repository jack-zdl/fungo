package com.fungo.system.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fungo.system.dao.MemberDao;
import com.fungo.system.entity.Member;
import com.fungo.system.facede.ICommunityProxyService;
import com.fungo.system.facede.IGameProxyService;
import com.fungo.system.service.MemberService;
import com.fungo.system.service.ScoreLogService;
import com.game.common.util.date.DateTools;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class MemberDataController {

	@Autowired
	private MemberDao memberDao;
	@Autowired
	private MemberService memberService;
	@Autowired
	private ScoreLogService scoreLogService;

	@Autowired
	private ICommunityProxyService communityProxyService;
	@Autowired
	private IGameProxyService gameProxyService;


	
	private static final Logger LOGGER = LoggerFactory.getLogger(MemberDataController.class);
	
	
	//连续7天未登录用户
	public void getUnLoginUsers() {
		
		//所有用户的最后登录时间(访问接口)
		
		//与今天相距7天
		
		//
	}
	
	
	
	@RequestMapping(value = "/api/inittaskhonor", method = RequestMethod.GET)
	//用户徽章
	public List<Map<String,Object>> initTaskHonor() throws Exception {
		//文章精选 评测精选 收到点赞
		
		LOGGER.info("开始添加用户徽章");
		//@todo 社区微服务
		//精品文章
		//List<Map> postMapList = new CopyOnWriteArrayList<>(); //memberDao.getHonorQualificationOfEssencePost();
		List<Map> postMapList = communityProxyService.getHonorQualificationOfEssencePost();

		//@todo 游戏微服务
		//精品评测
		List<Map> evaMapList = gameProxyService.getHonorQualificationOfEssenceEva();//new CopyOnWriteArrayList<>(); //memberDao.getHonorQualificationOfEssenceEva();
		
		//点赞
		List<Map> likeMapList = memberDao.getHonorQualificationOfBeLiked();
		
		List<Map<String,Object>> total = new ArrayList<>();
		
		for(Map postMap:postMapList) {
			Map<String,Object> map = new HashMap<>();
			map.put("member_id", postMap.get("member_id"));
			map.put("post_count", postMap.get("post_count"));
			total.add(map);
		}
		
		boolean b = false;
		for(Map evaMap:evaMapList) {
			for(Map<String,Object> map:total) {
				String memberId = (String) map.get("member_id");
				if(memberId.equals((String)evaMap.get("member_id"))) {//存在memberid的map 有没有包含在之前的map中
					b = true;
					map.put("eva_count", evaMap.get("eva_count"));
				}
			}
			if(b) {
				b = false;
				continue;
			}else {
				Map<String,Object> map = new HashMap<>();
				map.put("member_id", evaMap.get("member_id"));
				map.put("eva_count", evaMap.get("eva_count"));
				total.add(map);
			}
		}
		
		for(Map likeMap:likeMapList) {
			for(Map<String,Object> map:total) {
				String memberId = (String) map.get("member_id");
				if(memberId.equals((String)likeMap.get("member_id"))) {//存在memberid的map 有没有包含在之前的map中
					b = true;
					map.put("like_count", likeMap.get("like_count"));
				}
			}
			if(b) {
				b = false;
				continue;
			}else {
				Map<String,Object> map = new HashMap<>();
				map.put("member_id", likeMap.get("member_id"));
				map.put("like_count", likeMap.get("like_count"));
				total.add(map);
			}
		}
		exportExcel(total);
//		initHonor(total);
		LOGGER.info("添加完毕");
//		System.out.println(total);
		return total;
		
	}
	 
	/**
	  * 导出excel
	  * @param infoList
	  * @throws Exception
	  */
	 public void exportExcel(List<Map<String,Object>> infoList) throws Exception {
		 //文件
		 HSSFWorkbook workbook = new HSSFWorkbook();
		 
		 //sheet
	     
		 HSSFSheet sheet = workbook.createSheet("用户成就");
		
		 sheet.setColumnWidth(1, 4000);
	     sheet.setColumnWidth(2, 5000);
	     sheet.setColumnWidth(3, 4000);
	     sheet.setColumnWidth(4, 5000);
	     sheet.setColumnWidth(5, 4000);
	     
	     //row
	     HSSFRow row = sheet.createRow(0);
	     
	     //cell
//	     HSSFCellStyle style = workbook.createCellStyle();
//	     style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
	     
	     HSSFCell cell1 = row.createCell(0);
	     cell1.setCellValue("昵称");
	     HSSFCell cell2 = row.createCell(1);
	     cell2.setCellValue("手机号");
	     HSSFCell cell3 = row.createCell(2);
	     cell3.setCellValue("注册时间");
	     HSSFCell cell4 = row.createCell(3);
	     cell4.setCellValue("文章加精数");
	     HSSFCell cell5 = row.createCell(4);
	     cell5.setCellValue("游戏评论上安利墙数");
	     HSSFCell cell6 = row.createCell(5);
	     cell6.setCellValue("收到点赞数");
	     
	     int i = 1;
	     for(Map<String,Object> map:infoList) {
	    	 HSSFRow hssfRow = sheet.createRow(i);
	    	 //用户
	    	 Member member = memberService.selectById((String)map.get("member_id"));
	    	 if(member != null) {
	    		 HSSFCell c1 = hssfRow.createCell(0);//昵称
	    		 c1.setCellValue(member.getUserName());
	    		 HSSFCell c2 = hssfRow.createCell(1);//手机号
	    		 c2.setCellValue(member.getMobilePhoneNum());
	    		 HSSFCell c3 = hssfRow.createCell(2);//注册时间
	    		 c3.setCellValue(DateTools.fmtDate(member.getCreatedAt()));
	    		 
	    		 HSSFCell c4 = hssfRow.createCell(3);//post_count
	    		 if(map.get("post_count") != null ) {
//	    		 c4.setCellValue((double)map.get("post_count")); 
	    			 c4.setCellValue(map.get("post_count")+""); 
	    		 }else {
	    			 c4.setCellValue("未满足条件"); 
	    		 }
	    		 
	    		 HSSFCell c5 = hssfRow.createCell(4);//eva_count
	    		 if(map.get("eva_count") != null ) {
//	    		 c4.setCellValue((double)map.get("eva_count")); 
	    			 c5.setCellValue(map.get("eva_count")+""); 
	    		 }else {
//	    		 c4.setCellValue(-1); 
	    			 c5.setCellValue("未满足条件"); 
	    		 } 
	    		 
	    		 HSSFCell c6 = hssfRow.createCell(5);//liked_count
	    		 if(map.get("like_count") != null ) {
	    			 c6.setCellValue(map.get("like_count")+""); 
	    		 }else {
	    			 c6.setCellValue("未满足条件"); 
	    		 } 
	    	 }
	    	 
	    	 i++;
	     }
	     System.out.println(sheet);
	     OutputStream out = new FileOutputStream("E://用户成就.xls");
	     workbook.write(out);
	     out.close();
//	    System.out.println(cell);
	     
	 }
	 
	 
	 /**
	  * 初始化徽章
	  * @param infoList
	  * @throws Exception
	  */
	 public void initHonor(List<Map<String,Object>> infoList) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		 for(Map<String,Object> map:infoList) {
			 if(map.get("post_count") != null ) {
				int postCount = Integer.parseInt(map.get("post_count")+"");
				
				if(postCount >= 2 && postCount < 10) {//25
					
					LOGGER.info("用户id:{},徽章类型:{}",(String)map.get("member_id"),"会心一击1");
					scoreLogService.updateRanked((String)map.get("member_id"), mapper, 25);
				}else if(postCount >= 10 && postCount < 50) {//26
					
					LOGGER.info("用户id:{},徽章类型:{}",(String)map.get("member_id"),"会心一击2");
					scoreLogService.updateRanked((String)map.get("member_id"), mapper, 25);
					scoreLogService.updateRanked((String)map.get("member_id"), mapper, 26);
				}else if(postCount >= 50) {//27
					
					LOGGER.info("用户id:{},徽章类型:{}",(String)map.get("member_id"),"会心一击3");
					scoreLogService.updateRanked((String)map.get("member_id"), mapper, 25);
					scoreLogService.updateRanked((String)map.get("member_id"), mapper, 26);
					scoreLogService.updateRanked((String)map.get("member_id"), mapper, 27);
				}
			 }
			 
			 if(map.get("eva_count") != null ) {
				
				 int evaCount = Integer.parseInt(map.get("eva_count")+"");
				
				 if(evaCount >= 2 && evaCount < 10) {//28
					
					 LOGGER.info("用户id:{},徽章类型:{}",(String)map.get("member_id"),"拓荒者1");
					 scoreLogService.updateRanked((String)map.get("member_id"), mapper, 28);
					}else if(evaCount >= 10 && evaCount < 50) {//29
						
						LOGGER.info("用户id:{},徽章类型:{}",(String)map.get("member_id"),"拓荒者2");
						scoreLogService.updateRanked((String)map.get("member_id"), mapper, 28);
						scoreLogService.updateRanked((String)map.get("member_id"), mapper, 29);
					}else if(evaCount >= 50) {//30
						
						LOGGER.info("用户id:{},徽章类型:{}",(String)map.get("member_id"),"拓荒者3");
						scoreLogService.updateRanked((String)map.get("member_id"), mapper, 28);
						scoreLogService.updateRanked((String)map.get("member_id"), mapper, 29);
						scoreLogService.updateRanked((String)map.get("member_id"), mapper, 30);
					}
				 //
			 }
			 
			 if(map.get("like_count") != null ) {
				
				 int likedCount = Integer.parseInt(map.get("like_count")+"");
				 
				 if(likedCount >= 50 && likedCount < 100) {//31
					
					 LOGGER.info("用户id:{},徽章类型:{}",(String)map.get("member_id"),"神之手1");
					 scoreLogService.updateRanked((String)map.get("member_id"), mapper, 31);
					}else if(likedCount >= 100 && likedCount < 300) {//32
						 
						LOGGER.info("用户id:{},徽章类型:{}",(String)map.get("member_id"),"神之手2");
						scoreLogService.updateRanked((String)map.get("member_id"), mapper, 31);
						scoreLogService.updateRanked((String)map.get("member_id"), mapper, 32);
					}else if(likedCount >= 300) {//33
						
						LOGGER.info("用户id:{},徽章类型:{}",(String)map.get("member_id"),"神之手3");
						scoreLogService.updateRanked((String)map.get("member_id"), mapper, 31);
						scoreLogService.updateRanked((String)map.get("member_id"), mapper, 32);
						scoreLogService.updateRanked((String)map.get("member_id"), mapper, 33);
					}
				 //
			 }
			 
		 }
	 }
	 
	 public static void main(String[] args) {

	}
}
