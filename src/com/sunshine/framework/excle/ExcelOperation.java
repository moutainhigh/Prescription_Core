package com.sunshine.framework.excle;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFComment;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 支持excle的导入，导出按照，自定义模板，导出分为2003和2007.
 * 
 * @Project: presription_pharmacy
 * @Package: com.sunshine.platform.drug
 * @ClassName: ExcelOperation
 * @Description:
 *               <p>
 * 				支持百万级EXCLE数据导出,导入和导出Excel文件类 支持2003(xls)和2007(xlsx)版本的Excel文件
 *               </p>
 * @JDK version used:
 * @Author: 天竹子
 * @Create Date: 2017年9月10日
 * @modify By:
 * @modify Date:
 * @Why&What is modify:
 * @Version: 1.0
 */
public class ExcelOperation<T> {

	// public void exportExcel2003(String[] headers, Collection<T> dataset, OutputStream out) {
	// exportExcel("POI导出EXCEL文档", headers, dataset, out, "yyyy-MM-dd");
	// }
	//
	// public void exportExcel(String[] headers, Collection<T> dataset, OutputStream out, String pattern) {
	// exportExcel("POI导出EXCEL文档", headers, dataset, out, pattern);
	// }

	/**
	 * 对SXSSFWorkbook(int rowAccessWindowSize)解释如下： SXSSFWorkbook(int rowAccessWindowSize)官方源码：Construct an empty workbook and specify the window for
	 * row access. When a new node is created via createRow() and the total number of unflushed records would exceed the specified value, then the row
	 * with the lowest index value is flushed and cannot be accessed via getRow() anymore. A value of -1 indicates unlimited access. In this case all
	 * records that have not been flushed by a call to flush() are available for random access. A value of 0 is not allowed because it would flush any
	 * newly created row without having a chance to specify any cells.
	 */
	/**
	 * ###考虑到用户自动另存地址应该结合httpservlet通过response结合out设置相关的输出设置，但随之产生一个新问题就是out关闭问题，抽空再研究一下。 excle2007download入口.
	 * 
	 * @param title:
	 *            表格标题名
	 * @param headers:表格属性列名数组
	 * @param dataset:需要显示的数据集合,集合中一定要放置符合javabean风格的类的对象。此方法支持的javabean属性的数据类型有基本数据类型及String,Date,
	 * @param out:与输出设备关联的流对象，可以将EXCEL文档导出到本地文件或者网络中
	 * @param pattern:如果有时间数据，设定输出格式。默认为"yyy-MM-dd"
	 * @param downloadFields
	 *            需要下载的字段
	 */
	public void exportExcel2007(String tittle, String[] headers, Collection<T> dataset, HttpServletResponse response, String pattern,
			String[] downloadFields) {
		// XSSFWorkbook workbook = new XSSFWorkbook(); //使用XSS性能不是一般的差，连65535条数据都没法下载直接报内存溢出，无语了，还好jar包升级后解决了该问题。
		// 需要下载的字段
		String downloadField = "";
		// 标志符，每次写完一个置为false
		Boolean flag = true;
		if ("".equals(pattern) || pattern == null) {
			pattern = "yyyy-MM-dd";
		}
		Workbook workbook = new SXSSFWorkbook(500); // SXSSFWorkbook对象中存500条数据，开辟500数据的内存，进行复用以免内存溢出，其余写入硬盘(修改成3000条多线程调用的时候会内存会溢出，和虚拟机的内存内存设置有关)
		synchronized (workbook) {
			Sheet sheet = workbook.createSheet();
			sheet.setDefaultColumnWidth((short) 15);
			CellStyle style = workbook.createCellStyle();// 标题行样式
			style.setFillForegroundColor(HSSFColor.LIGHT_BLUE.index);
			style.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
			style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
			style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
			style.setBorderRight(XSSFCellStyle.BORDER_THIN);
			style.setBorderTop(XSSFCellStyle.BORDER_THIN);
			style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
			Font font = workbook.createFont();// 标题字体样式设置
			font.setColor((short) 0xFFFFFF);
			font.setFontHeightInPoints((short) 12);
			font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
			style.setFont(font);
			CellStyle style2 = workbook.createCellStyle();// 数据行样式
			style2.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
			style2.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
			style2.setBorderBottom(XSSFCellStyle.BORDER_THIN);
			style2.setBorderLeft(XSSFCellStyle.BORDER_THIN);
			style2.setBorderRight(XSSFCellStyle.BORDER_THIN);
			style2.setBorderTop(XSSFCellStyle.BORDER_THIN);
			style2.setAlignment(XSSFCellStyle.ALIGN_CENTER);
			style2.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);

			// 产生表格标题行
			Row row = sheet.createRow(0);
			for (short i = 0; i < headers.length; i++) {
				Cell cell = row.createCell(i);
				cell.setCellStyle(style);
				XSSFRichTextString text = new XSSFRichTextString(headers[i]);
				cell.setCellValue(text);
			}

			// 遍历集合数据，产生数据行
			Iterator<T> it = dataset.iterator();
			int index = 0;
			while (it.hasNext()) {
				index++;
				row = sheet.createRow(index);
				flag = true;
				T t = it.next();
				// 利用反射，根据javabean属性的先后顺序，动态调用getXXX()方法得到属性值ֵ
				Field[] fields = t.getClass().getDeclaredFields();
				for (int i = 0; i < downloadFields.length; i++) {
					downloadField = downloadFields[i];
					flag = true;
					for (short k = 0; k < fields.length && flag; k++) {
						Cell cell = row.createCell(i);
						cell.setCellStyle(style2);
						Field field = fields[k];// 遍历获取实体对象的字段
						String fieldName = field.getName();
						if (fieldName.equalsIgnoreCase(downloadField) && flag) {
							String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);// 获取getXXX()方法
							try {
								Class tCls = t.getClass();
								Method getMethod = tCls.getMethod(getMethodName, new Class[] {});
								Object value = getMethod.invoke(t, new Object[] {});// 通过getXXX()获取字段的返回类型
								// 判断值的类型后进行强制类型转换
								String textValue = null;
								if (value instanceof Date) {// 时间类型转换
									Date date = (Date) value;
									SimpleDateFormat sdf = new SimpleDateFormat(pattern);
									textValue = sdf.format(date);
								} else {
									// 其它数据类型都当作字符串简单处理
									if (value == null) {
										value = "";
									}
									textValue = value.toString();
								}
								// 利用正则表达式判断textValue是否全部由数字组成
								if (textValue != null) {
									Pattern p = Pattern.compile("^//d+(//.//d+)?$");
									Matcher matcher = p.matcher(textValue);
									if (matcher.matches()) {// 是数字当作double处理
										cell.setCellValue(Double.parseDouble(textValue));
									} else {
										XSSFRichTextString richString = new XSSFRichTextString(textValue);
										cell.setCellValue(richString);
									}
									// 将标识符置为false,避免先前写入的数据被覆盖
									flag = false;
								}
							} catch (SecurityException e) {
								e.printStackTrace();
							} catch (NoSuchMethodException e) {
								e.printStackTrace();
							} catch (IllegalArgumentException e) {
								e.printStackTrace();
							} catch (IllegalAccessException e) {
								e.printStackTrace();
							} catch (InvocationTargetException e) {
								e.printStackTrace();
							} finally {
								// 清理資源，目前暂时没有资源可以清理
							}
						} else {
							continue;
						}
					}
				}
			}

			try {
				response.setContentType("application/vnd.ms-excel");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				String dateString = sdf.format(new Date());
				tittle = tittle + dateString + ".xlsx";
				String fileName = null;
				String sOutputType = "attachment";
				fileName = new String(tittle.getBytes("GBK"), "iso8859-1");
				String sContentDisp = sOutputType + ";filename=" + fileName;
				response.addHeader("Content-Disposition", sContentDisp);
				OutputStream out = response.getOutputStream();
				workbook.write(out);
				out.flush();
				out.close(); // out如果close掉单机多线程调用的时候会报stream closed异常
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * excle2003 export.
	 * 
	 * @param title
	 *            sheet表格标题名
	 * @param headers
	 *            表格属性列名数组
	 * @param dataset
	 *            需要显示的数据集合,集合中一定要放置符合javabean风格的类的对象.
	 * @param out
	 *            与输出设备关联的流对象，可以将EXCEL文档导出到本地文件或者网络中
	 * @param pattern
	 *            如果有时间数据，设定输出格式。默认为"yyy-MM-dd"
	 * @param downloadFields
	 *            需要下载的字段
	 */
	@SuppressWarnings("unchecked")
	public void exportExcel2003(String tittle, String[] headers, Collection<T> dataset, HttpServletResponse response, String pattern,
			String[] downloadFields) {
		// 需要下载的字段
		String downloadField = "";
		// 标志符，每次写完一个置为false
		Boolean flag = true;
		if ("".equals(pattern) || pattern == null) {
			pattern = "yyyy-MM-dd";
		}
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet();
		sheet.setDefaultColumnWidth((short) 15);
		HSSFCellStyle style = workbook.createCellStyle();
		style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFFont font = workbook.createFont();
		font.setColor(HSSFColor.VIOLET.index);
		font.setFontHeightInPoints((short) 12);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		style.setFont(font);
		HSSFCellStyle style2 = workbook.createCellStyle();
		style2.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
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
		style2.setFont(font2);
		HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
		HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0, 0, 0, 0, (short) 4, 2, (short) 6, 5));
		comment.setString(new HSSFRichTextString("可以在POI中添加注释！"));
		// 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容.
		comment.setAuthor("leno");

		// 产生表格标题行
		HSSFRow row = sheet.createRow(0);
		for (short i = 0; i < headers.length; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellStyle(style);
			HSSFRichTextString text = new HSSFRichTextString(headers[i]);
			cell.setCellValue(text);
		}

		// 遍历集合数据，产生数据行
		Iterator<T> it = dataset.iterator();
		int index = 0;
		while (it.hasNext()) {
			index++;
			row = sheet.createRow(index);
			flag = true;
			T t = it.next();
			// 利用反射，根据javabean属性的先后顺序，动态调用getXxx()方法得到属性值ֵ
			Field[] fields = t.getClass().getDeclaredFields();
			for (int i = 0; i < downloadFields.length; i++) {
				downloadField = downloadFields[i];
				flag = true;
				for (short j = 0; j < fields.length && flag; j++) {
					HSSFCell cell = row.createCell(i);
					cell.setCellStyle(style2);
					Field field = fields[j];
					String fieldName = field.getName();
					if (downloadField.equalsIgnoreCase(fieldName)) {
						String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);// obtain getXXX() method
						// 当字段在需要下载的字段之列的时候进行下载否则继续下一循环的判断
						// if(downloadFields.contains(fieldName)){
						try {
							Class tCls = t.getClass();
							Method getMethod = tCls.getMethod(getMethodName, new Class[] {});
							Object value = getMethod.invoke(t, new Object[] {});// 通过反射获取字段值
							// 判断值的类型后进行强制类型转换
							String textValue = null;
							if (value instanceof Date) {
								Date date = (Date) value;
								SimpleDateFormat sdf = new SimpleDateFormat(pattern);
								textValue = sdf.format(date);
							} else {
								// 其它数据类型都当作字符串简单处理
								if (value == null) {
									value = "";
								}
								textValue = value.toString();
							}
							// 利用正则表达式判断textValue是否全部由数字组成
							if (textValue != null) {
								Pattern p = Pattern.compile("^//d+(//.//d+)?$");
								Matcher matcher = p.matcher(textValue);
								if (matcher.matches()) {
									// 是数字当作double处理
									cell.setCellValue(Double.parseDouble(textValue));
								} else {
									HSSFRichTextString richString = new HSSFRichTextString(textValue);
									cell.setCellValue(richString);
								}
								// 将标识符置为false,避免先前写入的数据被覆盖
								flag = false;
							}
						} catch (SecurityException e) {
							e.printStackTrace();
						} catch (NoSuchMethodException e) {
							e.printStackTrace();
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							e.printStackTrace();
						} finally {
							// 清理資源
						}
					} else {
						continue;
					}
				}
			}
		}
		try {
			response.setContentType("application/vnd.ms-excel");
			tittle = tittle + ".xls";
			String fileName = null;
			String sOutputType = "attachment";
			fileName = new String(tittle.getBytes("GBK"), "iso8859-1");
			String sContentDisp = sOutputType + ";filename=" + fileName;
			response.addHeader("Content-Disposition", sContentDisp);
			OutputStream out = response.getOutputStream();
			workbook.write(out);
			out.flush();
			out.close(); // out如果close掉单机多线程调用的时候会报stream closed异常
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * excle导入
	 * 
	 * @param execelFile
	 * @param clazz
	 * @param fieldsArray
	 *            需要导入的字段列
	 * @return
	 */
	public List<T> impExcel(String execelFile, Class<T> clazz, String[] fieldsArray) {
		List<T> list = new ArrayList<>();// 将所有的数据读取到List中
		String field = "";
		T t = null;

		try {
			// 构造 Workbook 对象，execelFile 是传入文件路径(获得Excel工作区)
			Workbook book = null;
			try {
				// Excel 2007获取方法
				book = new XSSFWorkbook(new FileInputStream(execelFile));
			} catch (Exception ex) {
				// Excel 2003获取方法
				book = new HSSFWorkbook(new FileInputStream(execelFile));
			}
			// 读取表格的第一个sheet页
			Sheet sheet = book.getSheetAt(0);
			// 定义 row、cell
			Row row;
			String returnValue = null;
			Boolean flag = true;
			// 总共有多少行,从0开始
			int totalRows = sheet.getLastRowNum();

			// 循环输出表格中的内容,首先循环取出行,再根据行循环取出列
			for (int i = 1; i <= totalRows; i++) {
				row = sheet.getRow(i);
				try {
					t = clazz.newInstance();
				} catch (InstantiationException e3) {
					e3.printStackTrace();
				} catch (IllegalAccessException e3) {
					e3.printStackTrace();
				}
				// 处理空行
				if (row == null) {
					continue;
				}
				// 总共有多少列,从0开始
				int totalCells = row.getLastCellNum();
				for (int j = row.getFirstCellNum(); j < totalCells; j++) {
					Cell cell = row.getCell(j);
					flag = true;
					Method method = null;
					field = fieldsArray[j];
					// ReflectionUtils.getAccessibleField(t, field);
					Field fields[] = concat(t.getClass().getDeclaredFields(), t.getClass().getSuperclass().getDeclaredFields());
					for (int k = 0; k < fields.length; k++) {
						String fieldName = null;
						String name = null;
						String type = null;

						if (flag) {
							fieldName = fields[k].getName();
							if (fieldName.equalsIgnoreCase(field) && flag) {
								name = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1); // 将属性的首字符大写，方便构造get，set方法
								type = fields[k].getType().getName();// 获取数据类型

								try {
									if (type.equals("java.lang.String")) {
										if (cell != null) {
											cell.setCellType(Cell.CELL_TYPE_STRING);
										}
										returnValue = cell.getStringCellValue().trim();
										if (StringUtils.isNotEmpty(returnValue)) {
											try {
												method = clazz.getMethod("set" + name, String.class);
												try {
													method.invoke(t, returnValue);
												} catch (IllegalAccessException e) {
													e.printStackTrace();
												} catch (InvocationTargetException e) {
													e.printStackTrace();
												}
											} catch (NoSuchMethodException e1) {
												e1.printStackTrace();
											}
										}
										flag = false;
									} else if (type.equals("java.lang.Integer")) {
										if (cell != null) {
											cell.setCellType(Cell.CELL_TYPE_NUMERIC);
										}
										Double doubleValue = cell.getNumericCellValue();

										try {
											method = clazz.getMethod("set" + name, Integer.class);
											try {
												method.invoke(t, Integer.valueOf(doubleValue.intValue()));
											} catch (IllegalAccessException e) {
												e.printStackTrace();
											} catch (InvocationTargetException e) {
												e.printStackTrace();
											}
										} catch (NoSuchMethodException e1) {
											e1.printStackTrace();
										}
										flag = false;
									} else if (type.equals("java.lang.Double")) {
										if (cell != null) {
											cell.setCellType(Cell.CELL_TYPE_NUMERIC);
										}
										Double doubleValue = cell.getNumericCellValue();

										try {
											method = clazz.getMethod("set" + name, Double.class);
											try {
												method.invoke(t, Double.valueOf(doubleValue.doubleValue()));
											} catch (IllegalAccessException e) {
												e.printStackTrace();
											} catch (InvocationTargetException e) {
												e.printStackTrace();
											}
										} catch (NoSuchMethodException e1) {
											e1.printStackTrace();
										}
										flag = false;
									} else if (type.equals("java.util.Date")) {
										String dateValue = cell.getStringCellValue();
										SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
										Date dateValueDate = null;
										try {
											dateValueDate = sdf.parse(dateValue);
										} catch (ParseException e2) {
											e2.printStackTrace();
										}

										try {
											method = clazz.getMethod("set" + name, Date.class);
											try {
												method.invoke(t, dateValueDate);
											} catch (IllegalAccessException e) {
												e.printStackTrace();
											} catch (InvocationTargetException e) {
												e.printStackTrace();
											}
										} catch (NoSuchMethodException e1) {
											e1.printStackTrace();
										}
										flag = false;
									} else if (type.equals("java.lang.Boolean")) {
										if (cell != null) {
											cell.setCellType(Cell.CELL_TYPE_BOOLEAN);
										}
										Boolean bool = cell.getBooleanCellValue();

										try {
											method = clazz.getMethod("set" + name, Boolean.class);
										} catch (NoSuchMethodException e1) {
											e1.printStackTrace();
										}
										try {
											method.invoke(t, bool);
										} catch (IllegalAccessException e) {
											e.printStackTrace();
										} catch (InvocationTargetException e) {
											e.printStackTrace();
										}
										flag = false;
									}

								} catch (SecurityException e) {
									e.printStackTrace();
								} catch (IllegalArgumentException e) {
									e.printStackTrace();
								}
							} else {
								continue;
							}
						}
					}
					flag = true;
				}
				list.add(t);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}

	static Field[] concat(Field[] a, Field[] b) {
		Field[] c = new Field[a.length + b.length];
		System.arraycopy(a, 0, c, 0, a.length);
		System.arraycopy(b, 0, c, a.length, b.length);
		return c;
	}
}