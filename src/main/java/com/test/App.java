package com.test;

import java.io.*;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

import javax.swing.text.DateFormatter;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Hello world!
 *
 */
//取得名稱，取得地址
public class App {
	
	static boolean reload = false;

	public static void main(String[] args) throws IOException {
		long startTime = System.currentTimeMillis();
		HashMap<String,String> map= new HashMap<String, String>();
		map.put("6", "中文字幕:6");
		map.put("2", "國產情色:2");
		map.put("4", "日本無碼:4");
		String typeList = null;
		System.out.println(map.values()); 
		do {
			System.out.println("請輸入產品編號："); 
			Scanner c = new Scanner(System.in);
			typeList = map.get(String.valueOf(c.nextInt())); // 設置分類
			if(typeList == null) {
				System.out.println("輸入編號有誤！"); 
			}
			
		}while(typeList == null);
		
		
		
		//String typeList = "中文字幕:5"; // 設置分類
		System.out.println("程序啓動中！請耐心等待！！！！！");

		for (String s : typeList.split(",")) {
			StringBuffer buffer = new StringBuffer();
			String[] type = s.split(":");
			String topicName = type[0];
			String topicId = type[1];
			reload = false;

			// 取得頁數
			Document document1;
			try {
				document1 = Jsoup.connect("http://uuu12.com/list/index" + topicId + ".html").get();
				Elements elements1 = document1.getElementsByAttributeValue("title", "尾页");
				String modifiDate = getLastDate(topicName);
				for(Element element :elements1 ) {
					String href = element.attr("href");
					Integer page = Integer.parseInt(href.substring(href.indexOf("_")+1, href.indexOf(".")));
					buffer.append("［分類：" + topicName + "］=====\r");
					for (int i = 1; i <= page; i++) {
						try {
							buffer.append("[第--" + i + "--頁] \r");
							Document document = null;
							if(i==1) {
								document = Jsoup.connect("http://uuu12.com/list/index" + topicId + ".html").get();
							}else {
								document = Jsoup.connect("http://uuu12.com/list/index" + topicId + "_" + i + ".html")
										.get();
							}
							Elements elements = document.getElementsByAttributeValue("class", "video-pic loading");
							if(!reload) {
								showNameAndUrl(elements, buffer,modifiDate);
							}else {
								break;
							}

						} catch (IOException e) {
							buffer.append("找不到：[第--" + i + "--頁]");
						}
					}
					saveAsFileWriter(buffer.toString(), topicName);
				}
				
				
			} catch (IOException e1) {

			}

			

		}

		long endTime = System.currentTimeMillis();
		System.out.println("程序运行时间：" + (endTime - startTime) / 1000/60 + "(分钟)");
		System.out.println("保存完成！" + new Date());
		System.out.println("保存完成！" + new Date());

	}
	
//	public static void main(String[] args) {
//		System.out.println(getLastDate("中1"));
//	}

	public static void showNameAndUrl(Elements elements, StringBuffer buffer,String modifiDate) {
		for(Element element :elements ) {
			Elements e = element.parent().getElementsByTag("span");
			String date = String.valueOf(e.get(2).html()).replaceAll("-", "");
			if(modifiDate!=null) {
				//如果文件修改日期大於數據記錄日期，就結束
				if(Integer.parseInt(modifiDate) >= Integer.parseInt(date)) {
					reload = true;
					return;
				}
			}
			
			String backgroundUrl = element.selectFirst("a").attr("style");
			String a = element.selectFirst("a").attr("href");
			String title = element.selectFirst("a").attr("title");
			String pic = backgroundUrl.replace("background: url(", "").replace(") no-repeat; background-position:50% 50%; background-size: cover;", "");
			
			System.out.println("文件日期:"+modifiDate+",資源日期："+date);
			String id = a.replace("/view/index", "").replace(".html", "");
			String url = "http://uuu12.com/play/index" + id + "-0-0.html";
			
			buffer.append("<img  src='"+pic+"'  />"+"片名：" + title + ",地址：<a href=\""+url+"\">" + url + "</a><br/>");
			
		}
		
		
		
	}

	private static void saveAsFileWriter(String content, String type) throws IOException {
		String filePath = jarPath()+"files/"+type + ".html";
		OutputStreamWriter op = new OutputStreamWriter(new FileOutputStream(filePath),"utf-8");
		op.append(content);
		op.flush();
		op.close();
	}
	
	/**
	 * 根據標題名稱，找到文件的最後修改日期
	 * @param topicName
	 * @return
	 */
	private static String getLastDate(String topicName) {
		//根據名稱類型找到對應該文。
		System.out.println(jarPath());
		File file = new File(jarPath()+"files");
		File[] tempLists = file.listFiles();
		String lastModifiedDate = null;
		for(File f : tempLists) {
			if(f.getName().indexOf(topicName)>=0) {
				lastModifiedDate = new SimpleDateFormat("yyyyMMdd").format(new Date(f.lastModified()));
				break;
			}
		}
		
		return lastModifiedDate;
	}
	
	private static String jarPath() {
		String path = System.getProperty("java.class.path");
		int firstIndex = path.lastIndexOf(System.getProperty("path.separator")) + 1;
		int lastIndex = path.lastIndexOf(File.separator) + 1;
		return path.substring(firstIndex, lastIndex);
	}
	
	
	
	
}
