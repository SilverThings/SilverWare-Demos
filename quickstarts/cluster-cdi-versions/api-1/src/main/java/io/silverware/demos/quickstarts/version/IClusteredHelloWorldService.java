package io.silverware.demos.quickstarts.version;

/**
 * @author Slavomír Krupa (slavomir.krupa@gmail.com)
 */
public interface IClusteredHelloWorldService {
   void hello();

   int magicCount(int a, int b);

   int magicCount(int a, Integer b);

   CustomObject customSerialization(CustomObject object);
}
