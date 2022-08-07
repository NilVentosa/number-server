# number-server

The number-server will start listening for clients in a configured port.
The number of simultaneous concurrent clients is limited and configurable.
Those clients can send input to the server, and it will be handled using the following rules:
+ The input will be processed after an application-native newline sequence.
+ If the input is a nine decimal digit sequence it will be processed.
+ If the input is the word 'terminate' the server will disconnect all clients and shut down.
+ If the input is anything else the server will disconnect that client.

Processed non duplicated lines will be added to a log file.

Every 10 seconds by default the server will print a report of the input that has been processed.

### How to compile

The project has been developed using Java 11 and Maven 3.8.6.

To compile the project to a jar file execute `mvn clean package`. The jar will be in the target folder.

### Usage

To run the server with default settings execute `java -jar number-server-1.0.jar`. 
The list of optional arguments and its defaults is below.

-f, --file-name=<fileName> File name to print the log of numbers. Default: numbers.log

-m, --max-connections=<maxConcurrentConnections> The maximum number of concurrent clients. Default: 5

-p, --port=<port> The port the server will listen to. Default: 4000

-r, --report-frequency=<reportFrequency> How often (in milliseconds) the report will be printed. Default: 10000

-h, --help Show this help message and exit.

-V, --version Print version information and exit.

### Assumptions

+ A line is considered to be terminated by any one of a line feed ('\n'), a carriage return ('\r'), 
a carriage return followed immediately by a line feed, or by reaching the end-of-file (EOF).
+ Duplicated nine decimal digit sequences are considered a valid input. 
For that reason they don't cause client disconnection. But they are not written to the file.
+ New clients are allowed to connect after reaching the active client limit, 
but no input is processed from that client until an active client disconnects.

# Task instructions
Using any of the following programming language (taking performance into consideration): Java, Kotlin, Python, Go, 
write a server ("Application") that opens a socket
and restricts input to at most 5 concurrent clients. Clients will connect to the Application and
write one or more numbers of 9 digit numbers, each number followed by a application-native newline
sequence, and then close the connection. The Application must write a de- duplicated list of
these numbers to a log file in no particular order.
Primary Considerations
+ The Application should work correctly as defined below in Requirements.
+ The overall structure of the Application should be simple.
+ The code of the Application should be descriptive and easy to read, and the build
method and runtime parameters must be well-described and work.
+ The design should be resilient with regard to data loss.
+ The Application should be optimized for maximum throughput, weighed along with the
other Primary Considerations and the Requirements below.

## Requirements
1. The Application must accept input from at most 5 concurrent clients on TCP/IP port 4000.
2. Input lines presented to the Application via its socket must either be composed of exactly nine 
   decimal digits (e.g.: 314159265 or 007007009) immediately followed by a
   application-native newline sequence; or a termination sequence as detailed in #9, below.
3. Numbers presented to the Application must include leading zeros as necessary to
   ensure they are each 9 decimal digits.
4. The log file, to be named "numbers.log", must be created anew and/or cleared when the
   Application starts.
5. Only numbers may be written to the log file. Each number must be followed by a
   application-native newline sequence.
6. No duplicate numbers may be written to the log file.
7. Any data that does not conform to a valid line of input should be discarded and the client
   connection terminated immediately and without comment.
8. Every 10 seconds, the Application must print a report to standard output:
   a. The difference since the last report of the count of new unique numbers that have
   been received.
   b. The difference since the last report of the count of new duplicate numbers that
   have been received.
   c. The total number of unique numbers received for this run of the Application.
   d. Example text for #8: Received 50 unique numbers, 2 duplicates. Unique total:
   567231
9. If any connected client writes a single line with only the word "terminate" followed by a
   application-native newline sequence, the Application must disconnect all clients and perform
   a clean shutdown as quickly as possible.
10. Clearly state all of the assumptions you made in completing the Application.
    
### Notes
+ You may write tests at your own discretion. Tests are useful to ensure your Application
    passes Primary Consideration A.
+ You may use common libraries in your project such as Apache Commons and Google
    Guava, particularly if their use helps improve Application simplicity and readability.
    However the use of large frameworks, such as Akka, is prohibited.
+ Your Application may not for any part of its operation use or require the use of external
    systems, for example Apache Kafka or Redis.
+ At your discretion, leading zeroes present in the input may be stripped—or not
    used—when writing output to the log or console.
+ Robust implementations of the Application typically handle more than 2M numbers per
    10-second reporting period on a modern MacBook Pro laptop (e.g.: 16 GiB of RAM and
    a 2.5 GHz Intel i7 processor).
+ To test if your application is working as expected, you can try to telnet to it through the
    port 4000 by executing: `telnet localhost 4000` and manually type in the numbers sequentially followed by a newline (enter).
