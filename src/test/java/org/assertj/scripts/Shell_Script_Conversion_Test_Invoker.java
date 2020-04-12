package org.assertj.scripts;

import org.junit.Assert;

import java.io.*;

public class Shell_Script_Conversion_Test_Invoker {
  private String fileName;
  private String filePath;
  private String fileDirectory;
  private String shellProgramLocation;
  private String testedShellPath;
  private String root;

  public Shell_Script_Conversion_Test_Invoker(String shellProgramLocation, String filePath, String testedShellPath) {
    // get the absolute path for this repository. If you have better method, please fix it.
    this.root = (new File("")).getAbsolutePath().replace(File.separator, "/");
    // It will open shell to execute shell script. it is the place sh.exe or shell.exe is.
    this.shellProgramLocation = shellProgramLocation;
    // the path of buffered file, it shall be deleted after invocation.
    this.filePath = filePath;
    // get the directory of file
    this.fileDirectory = filePath.substring(0,filePath.lastIndexOf('/')+1);
    // get the file name.
    this.fileName = filePath.substring(filePath.lastIndexOf('/')+1, filePath.length());
    // the path of the shell script it should test.
    this.testedShellPath = testedShellPath;
  }


  public void Start_Test(String input, String expected) throws Exception{
    try {
      write(input);
      Execute_Shell();
      String output = read();
      Assert.assertEquals(expected, output);
    }catch (Exception e) {
      throw e;
    }finally {
      delete();
    }
  }

  private void Execute_Shell() throws Exception{
    String shellCommand = "cd "+ root + "/" + fileDirectory + " && python " + root+ "/" + testedShellPath + " \"" + fileName + "\"";
    Runtime runtime = Runtime.getRuntime();
    Process pro = runtime.exec(new String[]{shellProgramLocation , "-c" ,shellCommand} , null , null);
    int status = pro.waitFor();
    if (status != 0) {
      throw new Exception("return status of shell script is nonzero");
    }
  }

  // read a file, if not found return null; Show log in server.

  private String read() throws Exception{
    String result = "";
    try (FileInputStream fis = new FileInputStream(filePath);) {
      byte[] buffer = new byte[fis.available()];
      fis.read(buffer);
      result = new String(buffer);
    }
    return result;
  }

  // append the text content behind the file. If file doesn't exist, create one.
  private void write(String newContent)throws IOException{
    File myFilePath = new File(filePath);
    boolean result = true;
    if (!myFilePath.exists()) {
       result = myFilePath.createNewFile();
    }

    if (result) {
      try (FileOutputStream fos = new FileOutputStream (filePath);){
        fos.write(newContent.getBytes());
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  // delete the file if file exist.
  private void delete() {
    try {
      File myFilePath = new File(filePath);
      if (myFilePath.exists()) {
        myFilePath.delete();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
