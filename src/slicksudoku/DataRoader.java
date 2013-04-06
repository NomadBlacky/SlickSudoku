package slicksudoku;

import java.io.File;

/**
 * 各種データをFileクラスで返す。
 */
public class DataRoader {

	/** imgディレクトリパス */
	String imgPath = "./data/img";
	
	/**
	 * 引数のファイル名と一致したFileクラスを返す。
	 * @param ファイル名
	 * @return 名前が一致したFileクラス（見つからなければnull）
	 */
	public File getFile(String name) {
		
		if(name == null)
			return null;
		
		File dir = new File(imgPath);
		File[] files = dir.listFiles();
		
		for(File f : files) {
			
			String fileName = f.getName();
			
			// 拡張子を含めて比較
			if(fileName.equals(name)) {
				return f;
			}
			
			// 拡張子を含めず比較
			int dot = fileName.lastIndexOf(".");
			fileName = fileName.substring(0, dot);
			if(fileName.equals(name)) {
				return f;
			}
		}
		
		return null;
	}
	
	public String getFilePath(String name) {
		
		File file = this.getFile(name);
		if(file == null) {
			return null;
		}
		return file.getPath();
	}
}
