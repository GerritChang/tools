package top.gerritchang.tools.utils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;

/**
 * 不同项目接口之间传送数据的工具类
 *
 * @author zhangguantao
 * @version 0.1
 */
public class HttpUtils {
	public static String uploadFile(String url, InputStream fileInputStream, String fileName) {
		String towHyphens = "--";   // 定义连接字符串
		String boundary = "******"; // 定义分界线字符串
		String end = "\r\n";    //定义结束换行字符串
		try {
			// 创建URL对象
			URL realUrl = new URL(url);
			// 获取连接对象
			URLConnection urlConnection = realUrl.openConnection();
			// 设置允许输入流输入数据到本机
			urlConnection.setDoOutput(true);
			// 设置允许输出流输出数据到服务器
			urlConnection.setDoInput(true);
			// 设置不使用缓存
			urlConnection.setUseCaches(false);
			// 设置请求参数中的内容类型为multipart/form-data,设置请求内容的分割线为******
			urlConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
			// 从连接对象中获取输出流
			OutputStream outputStream = urlConnection.getOutputStream();
			// 实例化数据输出流对象，将输出流传入
			DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
			// 遍历文件路径的长度,将路径数组下所有路径的文件都写到输出流中
			// 取出文件路径
			fileName = URLEncoder.encode(fileName, "utf-8");
			// 去掉文件名称的后缀
			fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
			// 向数据输出流中写出分割符
			dataOutputStream.writeBytes(towHyphens + boundary + end);
			// 向数据输出流中写出文件参数名与文件名
			dataOutputStream.writeBytes("Content-Disposition:form-data;name=file;filename=" + fileName + end);
			// 向数据输出流中写出结束标志
			dataOutputStream.writeBytes(end);
			// 定义缓冲区大小
			int bufferSize = 1024;
			// 定义字节数组对象，用来读取缓冲区数据
			byte[] buffer = new byte[bufferSize];
			// 定义一个整形变量，用来存放当前读取到的文件长度
			int length;
			// 循环从文件输出流中读取1024字节的数据，将每次读取的长度赋值给length变量，直到文件读取完毕，值为-1结束循环
			while ((length = fileInputStream.read(buffer)) != -1) {
				// 向数据输出流中写出数据
				dataOutputStream.write(buffer, 0, length);
			}
			// 每写出完成一个完整的文件流后，需要向数据输出流中写出结束标志符
			dataOutputStream.writeBytes(end);
			// 关闭文件输入流
			fileInputStream.close();
			// 向数据输出流中写出分隔符
			dataOutputStream.writeBytes(towHyphens + boundary + towHyphens + end);
			// 刷新数据输出流
			dataOutputStream.flush();
			// 从连接对象中获取字节输入流
			InputStream inputStream = urlConnection.getInputStream();
			// 实例化字符输入流对象，将字节流包装成字符流
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
			// 创建一个输入缓冲区对象，将要输入的字符流对象传入
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			// 创建一个字符串对象，用来接收每次从输入缓冲区中读入的字符串
			String line;
			// 创建一个可变字符串对象，用来装载缓冲区对象的最终数据，使用字符串追加的方式，将响应的所有数据都保存在该对象中
			StringBuilder stringBuilder = new StringBuilder();
			// 使用循环逐行读取缓冲区的数据，每次循环读入一行字符串数据赋值给line字符串变量，直到读取的行为空时标识内容读取结束循环
			while ((line = bufferedReader.readLine()) != null) {
				// 将缓冲区读取到的数据追加到可变字符对象中
				stringBuilder.append(line);
			}
			// 依次关闭打开的输入流
			bufferedReader.close();
			inputStreamReader.close();
			inputStream.close();
			// 依次关闭打开的输出流
			dataOutputStream.close();
			outputStream.close();
			// 返回服务器响应的数据
			return stringBuilder.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 获取GET拼接URL的工具方法
	 *
	 * @author zhangguantao
	 * @param params
	 * @return url?key=value或者url
	 * @version 0.1
	 */
	public static String getGETURL(Map<String, String> params) {
		String url = params.get("URL");
		if (params.size() > 1) {
			return getGETURL(url, params);
		} else {
			return url;
		}
	}

	public static String openRemoteEngineUrlResultString(String url, String params){
		StringBuffer sb = new StringBuffer();
		try {
			URL service = new URL(url);
			HttpURLConnection httpURLConnection = (HttpURLConnection) service.openConnection();
			httpURLConnection.setRequestMethod("POST");
			httpURLConnection.setRequestProperty("Content-Type","application/json");
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setUseCaches(false);

			StringBuffer param = new StringBuffer();
			param.append(params);
			OutputStream os = httpURLConnection.getOutputStream();
			os.write(param.toString().getBytes("utf-8"));
			os.flush();
			os.close();

			//获取返回的值
			InputStream is = httpURLConnection.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));

			do {
				sb.append(br.readLine());
			} while (br.read() != -1);
			br.close();
			is.close();
		}catch (IOException e){
			e.printStackTrace();
		}
		return sb.toString();
	}

	/**
	 * 获取GET拼接URL的工具方法
	 *
	 * @author zhangguantao
	 * @param     url,demo:http://192.168.1.1/
	 * @param params
	 * @return url?key=value
	 * @version 0.1
	 */
	public static String getGETURL(String url, Map<String, String> params) {
		StringBuffer buffer = new StringBuffer();
		int size = 1;
		buffer.append(url + "?");
		for (Map.Entry<String, String> entry : params.entrySet()) {
			if (entry.getKey() != "URL") {
				try {
					String param1 = URLEncoder.encode(entry.getKey(), "utf-8");
					buffer.append(param1);
					buffer.append("=");
					String param2 = URLEncoder.encode(entry.getValue(), "utf-8");
					buffer.append(param2);
					if (size < params.size()) {
						buffer.append("&");
					}
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
			size++;
		}
		String resultUrl = buffer.toString().replaceAll("\\r|\\n", "");
		return resultUrl;
	}

	/**
	 * 获取POST请求URL的工具方法
	 *
	 * @author zhangguantao
	 * @param params
	 * @return url
	 * @version 0.1
	 */
	public static String getPOSTURL(Map<String, String> params) {
		return params.get("URL").toString();
	}

	/**
	 * 发送GET请求复用的工具类
	 *
	 * @author zhangguantao
	 * @param        url,demo:http://192.168.1.1/
	 * @param params
	 * @return result
	 * @version 0.1
	 */
	public static String sendGETCommon(String url, Map<String, String> params) {
		String result = "";
		BufferedReader in = null;
		String realUrl = getGETURL(url, params);
		try {
			URL sendURL = new URL(realUrl);
			// 打开和URL之间的连接
			URLConnection conn = sendURL.openConnection();
			// 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("content-type", "text/plain");
            // 建立实际的连接
			conn.connect();
			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));
			// 设置读取响应
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 发送GET请求复用的工具类
	 *
	 * @author zhangguantao
	 * @param        url,demo:http://192.168.1.1/
	 * @return result
	 * @version 0.1
	 */
	public static String sendGETCommon(String url) {
		String result = "";
		BufferedReader in = null;
		try {
			URL sendURL = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = sendURL.openConnection();
			// 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 建立实际的连接
			conn.connect();
			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			// 设置读取响应
			String line = null;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 发送GET请求复用的工具类
	 *
	 * @author zhangguantao
	 * @param        url,demo:http://192.168.1.1/
	 * @param params
	 * @return result
	 * @version 0.1
	 */
	public static String sendGETCommon(String url, Map<String, String> params, String encode) {
		String result = "";
		BufferedReader in = null;
		String realUrl = getGETURL(url, params);
		try {
			URL sendURL = new URL(realUrl);
			// 打开和URL之间的连接
			URLConnection conn = sendURL.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			conn.setRequestProperty("Content-Type", "application/xml; charset=" + encode);
			// 建立实际的连接
			conn.connect();
			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(),encode));
			// 设置读取响应
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 发送GET请求的工具类
	 *
	 * @author zhangguantao
	 * @param        url,demo:http://192.168.1.1/
	 * @param params
	 * @return true(发送成功)|false(发送失败)
	 * @version 0.1
	 */
	public static Boolean sendGET(String url, Map<String, String> params) {
		String realUrl = getGETURL(url, params);
		String result = sendGETCommon(realUrl);
		if (result.indexOf("true") != -1) {
			return true;
		}
		return false;
	}

	/**
	 * 发送GET请求的工具类
	 *
	 * @author zhangguantao
	 * @param params
	 * @return true(发送成功)|false(发送失败)
	 * @version 0.1
	 */
	public static Boolean sendGET(Map<String, String> params) {
		String realUrl = getGETURL(params);
		String result = sendGETCommon(realUrl);
		if (result.indexOf("true") != -1) {
			return true;
		}
		return false;
	}

	/**
	 * 设置POST请求参数的工具类
	 *
	 * @author zhangguantao
	 * @param map
	 * @return key=value&key=value
	 * @version 0.1
	 */
	public static String setPostParamsToKeyAndValue(Map<String, String> map) {
		if(map.isEmpty()) {
			return null;
		}else {
			StringBuffer buffer = new StringBuffer();
			for (Map.Entry<String, String> m : map.entrySet()) {
				if (m.getKey() != "URL") {
					buffer.append(m.getKey() + "=" + m.getValue());
					buffer.append("&");
				}
			}
			String string = buffer.toString();
			String resultParam = string.substring(0, string.length() - 1);
			return resultParam;
		}
	}

	/**
	 * 设置POST请求参数的工具类
	 *
	 * @author zhangguantao
	 * @param map
	 * @return json
	 * @version 0.1
	 */
//	public static String setPostParamsToJson(Map<String, String> map) {
//        Object str = JSONObject.toJSON(map);
//        return str.toString();
//	}

	/**
	 * 发送POST请求的复用工具类
	 *
	 * @author zhangguantao
	 * @param url
	 * @param params
	 * @return result
	 * @version 0.1
	 */
	public static String sendPOSTCommon(String url, String params) {
		String result = "";
		PrintWriter out = null;
		BufferedReader in = null;
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(new OutputStreamWriter(conn.getOutputStream(), "utf-8"));
			// 发送请求参数
			out.print(params);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}


	/**
	 * 发送POST请求的工具类
	 *
	 * @author zhangguantao
	 * @param url
	 * @param map
	 * @return true(发送成功)|false(发送失败)
	 * @version 0.1
	 */
	public static String sendPOST(String url, Map<String, String> map) {
		String postParams = setPostParamsToKeyAndValue(map);
        return sendPOSTCommon(url, postParams);
	}

	/**
	 * 发送POST请求的工具类
	 *
	 * @author zhangguantao
	 * @param url
	 * @param string
	 * @return true(发送成功)|false(发送失败)
	 * @version 0.1
	 */
	public static String sendPOST(String url, String string) {
        return sendPOSTCommon(url, string);
	}

	/**
	 * 发送POST请求的工具类
	 *
	 * @author zhangguantao
	 * @param map
	 * @return true(发送成功)|false(发送失败)
	 * @version 0.1
	 */
	public static String sendPOST(Map<String, String> map) {
		String postParams = setPostParamsToKeyAndValue(map);
		String url = getPOSTURL(map);
        return sendPOSTCommon(url, postParams);
	}

	/**
	 * 发送POST请求的工具类
	 *
	 * @author zhangguantao
	 * @param url
	 * @param map
	 * @return true(发送成功)|false(发送失败)
	 * @version 0.1
	 */
//	public static String sendPOSTByJson(String url, Map<String, String> map) {
//		String postParams = setPostParamsToJson(map);
//        return sendPOSTCommon(url, postParams);
//	}

	/**
	 * 发送POST请求的工具类
	 *
	 * @author zhangguantao
	 * @param map
	 * @return true(发送成功)|false(发送失败)
	 * @version 0.1
	 */
	public static String sendPOSTByJson(Map<String, String> map) {
		String postParams = setPostParamsToKeyAndValue(map);
		String url = getPOSTURL(map);
        return sendPOSTCommon(url, postParams);
	}
}
