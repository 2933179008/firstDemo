package com.tbl.modules.platform.util;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
/**
 * 导出excel公用
 * @author 赵永杰
 */
public class DeriveExcel {


	@SuppressWarnings("deprecation")
	public static boolean exportExcel(String sheetName,List<?> list,Map<String, String> mapFields,HttpServletResponse response,String path) throws IOException{
		boolean result = false;
		List<String> methodNameList = new ArrayList<String>();
		//声明一个工作簿
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 生成一个表格
		HSSFSheet sheet = workbook.createSheet(sheetName);
		// 设置表格默认列宽度为15个字节
		sheet.setDefaultColumnWidth((short) 15);
		// 生成一个样式	
		HSSFCellStyle style = workbook.createCellStyle();       
		// 设置这些样式
		style.setFillForegroundColor(HSSFColor.WHITE.index);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// 生成一个字体
		HSSFFont font = workbook.createFont();
		font.setColor(HSSFColor.BLACK.index);
		font.setFontHeightInPoints((short) 12);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		// 把字体应用到当前的样式
		style.setFont(font);
		// 生成并设置另一个样式
		HSSFCellStyle style2 = workbook.createCellStyle();
		style2.setFillForegroundColor(HSSFColor.WHITE.index);
		style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		// 生成另一个字体
		HSSFFont font2 = workbook.createFont();
		font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		//产生表格标题行
		HSSFRow row = sheet.createRow(0);
		try {
			int columnIndex = 0;  //列索引
			if (mapFields != null) {
				Object objClass = null;
				Method method = null;
				Map<String, Method> getMap = null;
				for (Iterator<String> i = mapFields.keySet().iterator(); i.hasNext(); ) {
					String key = i.next();
					HSSFCell cell = row.createCell(columnIndex);
					cell.setCellStyle(style);
					cell.setCellValue(mapFields.get(key));
					//记录字段的顺序，以便于导出的内容与字段不出现偏移
					methodNameList.add(key);
					columnIndex++;
				}
				//遍历集合数据，产生数据行
				if (list != null && list.size() > 0) {
					//导出表格内容
					for (int i = 0, len = list.size(); i < len; i++) {
						row = sheet.createRow(i+1);
						objClass = list.get(i);
						//获得对象所有的get方法
						getMap = getAllMethod(objClass);
						for (int j = 0; j < methodNameList.size(); j++) {
							//根据key获取对应方法
							method = getMap.get("GET" + methodNameList.get(j).toString().toUpperCase());
							if(method!=null){
								//从对应的get方法得到返回值
								if(method.invoke(objClass) != null){
									String value = method.invoke(objClass).toString();
									//应用wcfc样式创建单元格
									HSSFCell cell = row.createCell(j);
									cell.setCellStyle(style2);
									cell.setCellValue(value);
								}else{
									String value = "";
									//应用wcfc样式创建单元格
									HSSFCell cell = row.createCell(j);
									cell.setCellStyle(style2);
									cell.setCellValue(value);
								}
							}else{
								String value = "";
								//应用wcfc样式创建单元格
								HSSFCell cell = row.createCell(j);
								cell.setCellStyle(style2);
								cell.setCellValue(value);
							}
						}

					}
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		try {
			OutputStream ouputStream = response.getOutputStream();
			workbook.write(ouputStream);
			ouputStream.flush();
			ouputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		workbook.close();
		return result;
	}

//	public static boolean exportExcel(String sheetName,List<?> list,Map<String, String> mapFields,
//			HttpServletResponse response,String path){
//		boolean result = false;
//		WritableWorkbook wook = null;//可写的工作薄对象
//        Object objClass = null;
//        OutputStream out = null;
//		try{
//			if(path != null && !"".equals(path)){
//				File file = new File(path);
//				if(!file.exists()){
//					file.mkdirs();
//				}
//				out = new FileOutputStream(file + "\\" + sheetName);
//			}else{
//				out = response.getOutputStream();
//			}
//			wook = Workbook.createWorkbook(out);
//            //定义格式 字体 下划线 斜体 粗体 颜色
//			//定义头样式
//            WritableFont font = new WritableFont(WritableFont.ARIAL, 11,
//                    WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE,Colour.BLACK);
//            WritableCellFormat wcf_title = new WritableCellFormat(font);
//            wcf_title.setAlignment(Alignment.CENTRE);
//            wcf_title.setVerticalAlignment(VerticalAlignment.CENTRE);
//            wcf_title.setBorder(Border.ALL,BorderLineStyle.THIN,Colour.BLACK);
//            wcf_title.setWrap(false);//不自动换行
//            // 定义表格样式
//            WritableFont tableFont = new WritableFont(WritableFont.ARIAL,10,
//                    WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE,Colour.BLACK);
//            WritableCellFormat wcf_table = new WritableCellFormat(tableFont);
//            wcf_table.setAlignment(Alignment.CENTRE);
//            wcf_table.setVerticalAlignment(VerticalAlignment.CENTRE);
//            wcf_table.setBorder(Border.ALL,BorderLineStyle.THIN,Colour.BLACK);
//            wcf_table.setWrap(false);//不自动换行
//            //创建工作表
//            WritableSheet sheet = wook.createSheet(sheetName, 0);
//            SheetSettings setting = sheet.getSettings();
//            setting.setVerticalFreeze(1);//冻结窗口头部
//
//            int columnIndex = 0;  //列索引
//            List<String> methodNameList = new ArrayList<String>();
//            if(mapFields != null){
//            	String key  = "";
//            	Map<String,Method> getMap = null;
//            	Method method = null;
//            	//开始导出表格头部
//            	for (Iterator<String> i = mapFields.keySet().iterator();i.hasNext();) {
//            		key = i.next();
//            		/*表格头样式导出*/
//            		sheet.setColumnView(columnIndex, 20);//根据内容自动设置列宽
//            		sheet.addCell(new Label(columnIndex, 0, mapFields.get(key), wcf_title));
//            		//记录字段的顺序，以便于导出的内容与字段不出现偏移
//                    methodNameList.add(key);
//                    columnIndex++;
//            	}
//            	if(list != null && list.size() > 0){
//            		//导出表格内容
//                    for (int i = 0,len = list.size(); i < len; i++) {
//                    	objClass = list.get(i);
//                    	//获得对象所有的get方法
//                    	getMap = getAllMethod(objClass);
//                    	//按保存的字段顺序导出内容
//                    	for (int j = 0; j < methodNameList.size(); j++) {
//                    		//根据key获取对应方法
//                            method = getMap.get("GET" + methodNameList.get(j).toString().toUpperCase());
//                            if(method!=null){
//                            	//从对应的get方法得到返回值
//                            	if(method.invoke(objClass) != null){
//                            		String value = method.invoke(objClass).toString();
//                                    //应用wcfc样式创建单元格
//                                    sheet.addCell(new Label(j, i+1, value, wcf_table));
//                            	}else{
//                            		sheet.addCell(new Label(j, i+1, "", wcf_table));
//                            	}
//                            }else{
//                                //如果没有对应的get方法，则默认将内容设为""
//                                sheet.addCell(new Label(j, i+1, "", wcf_table));
//                            }
//                    	}
//                    }
//            	}else{
//            		System.out.println("导出表格无数据！");
//            	}
//            	result = true;
//            }
//            wook.write();
//		}catch (Exception e) {
//			result = false;
//			System.out.println("失败");
//		} finally{
//            try {
//                if(wook!=null){
//                    wook.close();
//                }
//                if(out!=null){
//                	out.flush();
//                    out.close();
//                }
//            } catch (Exception e2) {
//                e2.printStackTrace();
//            }
//        }
//		return result;
//	}
	
	/**
     * 获取类的所有get方法
     * @param obj
     * @return
     */
	public static HashMap<String,Method> getAllMethod(Object obj) 
			throws Exception{
        HashMap<String,Method> map = new HashMap<String,Method>();
        Method[] methods = obj.getClass().getMethods();//得到所有方法
        String methodName = "";
        for (int i = 0; i < methods.length; i++) {
            methodName = methods[i].getName().toUpperCase();//方法名
            if(methodName.startsWith("GET")){
                map.put(methodName, methods[i]);//添加get方法至map中
            }
        }
        return map;
    }
	
	/**
	 * 导出表格，可针对修改表格长度
	 * @param sheetName
	 * @param list
	 * @param mapFields
	 * @param response
	 * @param path
	 * @param lent 每列的宽度的数组
	 * @return
	 */
//	public static boolean exportExcelForLen(String sheetName,List<?> list,Map<String, String> mapFields,
//			HttpServletResponse response,String path,int[] lent){
//		boolean result = false;
//		WritableWorkbook wook = null;//可写的工作薄对象
//        Object objClass = null;
//        OutputStream out = null;
//		try{
//			if(path != null && !"".equals(path)){
//				File file = new File(path);
//				if(!file.exists()){
//					file.mkdirs();
//				}
//				out = new FileOutputStream(file + "\\" + sheetName);
//			}else{
//				out = response.getOutputStream();
//			}
//			wook = Workbook.createWorkbook(out);
//            //定义格式 字体 下划线 斜体 粗体 颜色
//			//定义头样式
//            WritableFont font = new WritableFont(WritableFont.ARIAL, 11,
//                    WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE,Colour.BLACK);
//            WritableCellFormat wcf_title = new WritableCellFormat(font);
//            wcf_title.setAlignment(Alignment.CENTRE);
//            wcf_title.setVerticalAlignment(VerticalAlignment.CENTRE);
//            wcf_title.setBorder(Border.ALL,BorderLineStyle.THIN,Colour.BLACK);
//            wcf_title.setWrap(false);//不自动换行
//            // 定义表格样式
//            WritableFont tableFont = new WritableFont(WritableFont.ARIAL,10,
//                    WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE,Colour.BLACK);
//            WritableCellFormat wcf_table = new WritableCellFormat(tableFont);
//            wcf_table.setAlignment(Alignment.CENTRE);
//            wcf_table.setVerticalAlignment(VerticalAlignment.CENTRE);
//            wcf_table.setBorder(Border.ALL,BorderLineStyle.THIN,Colour.BLACK);
//            wcf_table.setWrap(false);//不自动换行
//            //创建工作表
//            WritableSheet sheet = wook.createSheet(sheetName, 0);
//            SheetSettings setting = sheet.getSettings();
//            setting.setVerticalFreeze(1);//冻结窗口头部
//
//            int columnIndex = 0;  //列索引
//            List<String> methodNameList = new ArrayList<String>();
//            if(mapFields != null){
//            	String key  = "";
//            	Map<String,Method> getMap = null;
//            	Method method = null;
//            	//开始导出表格头部
//            	int num = 0;
//            	for (Iterator<String> i = mapFields.keySet().iterator();i.hasNext();) {
//            		key = i.next();
//            		/*表格头样式导出*/
//            		sheet.setColumnView(columnIndex, lent[num]);//根据内容自动设置列宽
//            		num++;
//            		sheet.addCell(new Label(columnIndex, 0, mapFields.get(key), wcf_title));
//            		//记录字段的顺序，以便于导出的内容与字段不出现偏移
//                    methodNameList.add(key);
//                    columnIndex++;
//            	}
//            	if(list != null && list.size() > 0){
//            		//导出表格内容
//                    for (int i = 0,len = list.size(); i < len; i++) {
//                    	objClass = list.get(i);
//                    	//获得对象所有的get方法
//                    	getMap = getAllMethod(objClass);
//                    	//按保存的字段顺序导出内容
//                    	for (int j = 0; j < methodNameList.size(); j++) {
//                    		//根据key获取对应方法
//                            method = getMap.get("GET" + methodNameList.get(j).toString().toUpperCase());
//                            if(method!=null){
//                            	//从对应的get方法得到返回值
//                            	if(method.invoke(objClass) != null){
//                            		String value = method.invoke(objClass).toString();
//                                    //应用wcfc样式创建单元格
//                                    sheet.addCell(new Label(j, i+1, value==null?"":(Objects.equal(value,"null")?"":value), wcf_table));
//								}else{
//                            		sheet.addCell(new Label(j, i+1, "", wcf_table));
//                            	}
//                            }else{
//                                //如果没有对应的get方法，则默认将内容设为""
//                                sheet.addCell(new Label(j, i+1, "", wcf_table));
//                            }
//                    	}
//                    }
//            	}else{
//            		System.out.println("导出表格无数据！");
//            	}
//            	result = true;
//            }
//            wook.write();
//		}catch (Exception e) {
//			result = false;
//			System.out.println("失败");
//		} finally{
//            try {
//                if(wook!=null){
//                    wook.close();
//                }
//                if(out!=null){
//                	out.flush();
//                    out.close();
//                }
//            } catch (Exception e2) {
//                e2.printStackTrace();
//            }
//        }
//		return result;
//	}
	/**
	 * 导出并合并单元格
	 * @param sheetName
	 * @param list
	 * @param mapFields
	 * @param response
	 * @param path
	 * @return
	 */
//	public static boolean exportExcelComBine(String sheetName,List<?> list,Map<String, String> mapFields,
//			HttpServletResponse response,String path){
//		boolean result = false;
//		WritableWorkbook wook = null;//可写的工作薄对象
//        Object objClass = null;
//        OutputStream out = null;
//		try{
//			if(path != null && !"".equals(path)){
//				File file = new File(path);
//				if(!file.exists()){
//					file.mkdirs();
//				}
//				out = new FileOutputStream(file + "\\" + sheetName);
//			}else{
//				out = response.getOutputStream();
//			}
//			wook = Workbook.createWorkbook(out);
//            //定义格式 字体 下划线 斜体 粗体 颜色
//			//定义头样式
//            WritableFont font = new WritableFont(WritableFont.ARIAL, 11,
//                    WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE,Colour.BLACK);
//            WritableCellFormat wcf_title = new WritableCellFormat(font);
//            wcf_title.setAlignment(Alignment.CENTRE);
//            wcf_title.setVerticalAlignment(VerticalAlignment.CENTRE);
//            wcf_title.setBorder(Border.ALL,BorderLineStyle.THIN,Colour.BLACK);
//            wcf_title.setWrap(false);//不自动换行
//            // 定义表格样式
//            WritableFont tableFont = new WritableFont(WritableFont.ARIAL,10,
//                    WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE,Colour.BLACK);
//            WritableCellFormat wcf_table = new WritableCellFormat(tableFont);
//            wcf_table.setAlignment(Alignment.CENTRE);
//            wcf_table.setVerticalAlignment(VerticalAlignment.CENTRE);
//            wcf_table.setBorder(Border.ALL,BorderLineStyle.THIN,Colour.BLACK);
//            wcf_table.setWrap(false);//不自动换行
//            //创建工作表
//            WritableSheet sheet = wook.createSheet(sheetName, 0);
//            SheetSettings setting = sheet.getSettings();
//            setting.setVerticalFreeze(1);//冻结窗口头部
//
//            int columnIndex = 0;  //列索引
//            List<String> methodNameList = new ArrayList<String>();
//            if(mapFields != null){
//            	String key  = "";
//            	Map<String,Method> getMap = null;
//            	Method method = null;
//            	//开始导出表格头部
//            	for (Iterator<String> i = mapFields.keySet().iterator();i.hasNext();) {
//            		key = i.next();
//            		System.out.println(key);
//            		/*表格头样式导出*/
//            		sheet.setColumnView(columnIndex, 20);//根据内容自动设置列宽
//            		sheet.addCell(new Label(columnIndex, 0, mapFields.get(key), wcf_title));
//            		//记录字段的顺序，以便于导出的内容与字段不出现偏移
//                    methodNameList.add(key);
//                    columnIndex++;
//            	}
//            	if(list != null && list.size() > 0){
//            		//导出表格内容
//                    for (int i = 0,len = list.size(); i < len; i++) {
//                    	objClass = list.get(i);
//                    	//获得对象所有的get方法
//                    	getMap = getAllMethod(objClass);
//                    	//按保存的字段顺序导出内容
//                    	for (int j = 0; j < methodNameList.size(); j++) {
//                    		//根据key获取对应方法
//                            method = getMap.get("GET" + methodNameList.get(j).toString().toUpperCase());
//                            if(method!=null){
//                            	//从对应的get方法得到返回值
//                            	if(method.invoke(objClass) != null){
//                            		String value = method.invoke(objClass).toString();
//                                    //应用wcfc样式创建单元格
//                                    sheet.addCell(new Label(j, i+1, value, wcf_table));
//                            	}else{
//                            		sheet.addCell(new Label(j, i+1, "", wcf_table));
//                            	}
//                            }else{
//                                //如果没有对应的get方法，则默认将内容设为""
//                                sheet.addCell(new Label(j, i+1, "", wcf_table));
//                            }
//                    	}
//                    }
//            	}else{
//            		System.out.println("导出表格无数据！");
//            	}
//            	result = true;
//            }
//            wook.write();
//		}catch (Exception e) {
//			result = false;
//			System.out.println("失败");
//		} finally{
//            try {
//                if(wook!=null){
//                    wook.close();
//                }
//                if(out!=null){
//                	out.flush();
//                    out.close();
//                }
//            } catch (Exception e2) {
//                e2.printStackTrace();
//            }
//        }
//		return result;
//	}
	
	
	
}
