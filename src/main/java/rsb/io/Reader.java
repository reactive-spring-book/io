package rsb.io;

import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

interface Reader {

	void read(File file, Consumer<Bytes> consumer, Runnable finished) throws IOException;

}
