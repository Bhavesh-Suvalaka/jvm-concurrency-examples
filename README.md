# Example of using virtual thread in Spring boot application
* Refer to @ThreadConfig class to override default executor service, this only needed for
 SpringBoot version until 3.1.X, after spring boot 3.2.X, the support for virtual thread is
 given out of the box, you just need to set spring.threads.virtual.enabled to true.

## Reference links:
[Loom Proposal](https://cr.openjdk.org/~rpressler/loom/Loom-Proposal.html) <br/>
[Second Preview](https://openjdk.org/jeps/425) <br/>
[Documentations](https://docs.oracle.com/en/java/javase/21/core/virtual-threads.html#GUID-DC4306FC-D6C1-4BCC-AECE-48C32C1A8DAA)<br/>
[Loom notes](https://jdk.java.net/loom/) <br/>
[Sprinboot Adoption](https://www.infoq.com/news/2023/12/spring-boot-virtual-threads/#:~:text=Spring%20Boot%203.2%20has%20integrated,now%20operate%20on%20virtual%20threads) <br/>
[All Loom Related blogs](https://inside.java/tag/loom) <br/>
[GitHub Repo](https://github.com/Bhavesh-Suvalaka/loom-demo) <br/>

