package com.baocloud.mul;

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;

public class Piped {
	static class Print implements Runnable {
		private PipedReader pr;

		public Print(PipedReader pr) {
			this.pr = pr;
		}

		@Override
		public void run() {
			int receive = 0;
			try {
				while ((receive = pr.read()) != -1) {
					System.out.print((char) receive);
				}
			} catch (IOException e) {
			}

		}

	}
	
	public static void main(String[] args) throws IOException {
		PipedReader pr=new PipedReader();
		PipedWriter pw=new PipedWriter();
		pw.connect(pr);
		Thread printT=new Thread(new Print(pr), "Print");
		printT.start();
		int receive=0;
		try{
			while((receive=System.in.read()) !=-1){
				pw.write(receive);
			}
		}finally{
			pw.close();
		}
	}
}
