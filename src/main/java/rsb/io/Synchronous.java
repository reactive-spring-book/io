package rsb.io;

import lombok.extern.log4j.Log4j2;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.function.Consumer;

@Log4j2
class Synchronous implements Reader {

	@Override
	public void read(File file, Consumer<Bytes> consumer, Runnable f) throws IOException {
		try (FileInputStream in = new FileInputStream(file)) { // <1>
			byte[] data = new byte[FileCopyUtils.BUFFER_SIZE];
			int res;
			while ((res = in.read(data, 0, data.length)) != -1) { // <2>
				consumer.accept(Bytes.from(data, res)); // <3>
			}
			f.run();
		}
	}

}
