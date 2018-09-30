package rsb.io;

import lombok.extern.log4j.Log4j2;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;

import java.io.*;
import java.nio.file.Files;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

@Log4j2
public class IoTest {

	private final AtomicLong count = new AtomicLong();

	private final Consumer<Bytes> bytesConsumer = bytes -> this.count.getAndAccumulate(
			bytes.getLength(),
			(previousValue, updateValue) -> previousValue + updateValue);

	private final Resource resource = new ClassPathResource("/data.txt");

	private final Io io = new Io();

	private final File file = Files.createTempFile("io-test-data", ".txt").toFile();

	private final CountDownLatch latch = new CountDownLatch(1);

	private final Runnable onceDone = () -> {
		log.info("counted " + this.count.get() + " bytes");
		this.latch.countDown();
	};

	public IoTest() throws IOException {
	}

	@Before
	public void before() throws IOException {
		this.count.set(0);
		try (InputStream in = this.resource.getInputStream();
				OutputStream out = new FileOutputStream(this.file)) {
			FileCopyUtils.copy(in, out);
		}
	}

	@After
	public void tearDown() {
		if (this.file.exists()) {
			Assert.assertTrue(this.file.delete());
		}
	}

	@Test
	public void synchronousRead() {
		test(() -> this.io.synchronousRead(this.file, this.bytesConsumer, this.onceDone));
	}

	@Test
	public void asynchronousRead() {
		test(() -> this.io.asynchronousRead(this.file, this.bytesConsumer,
				this.onceDone));
	}

	private void test(Runnable r) {
		try {
			r.run();
			this.latch.await();
			Assert.assertEquals(this.count.get(), this.file.length());
		}
		catch (InterruptedException e) {
			log.error(e);
		}

	}

}