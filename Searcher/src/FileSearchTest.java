import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

public class FileSearchTest {

	@Test
	public void testSearch() {
		FileSearch fs = new FileSearch();
		File ff = new File ("D:\\");
		assertEquals(true,fs.searchDirectory(ff, "Description"));
	}

}
