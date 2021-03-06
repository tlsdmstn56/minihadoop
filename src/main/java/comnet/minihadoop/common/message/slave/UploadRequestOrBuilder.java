// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: src/main/proto/slave.proto

package comnet.minihadoop.common.message.slave;

public interface UploadRequestOrBuilder extends
    // @@protoc_insertion_point(interface_extends:slave.UploadRequest)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>string clientHostname = 1;</code>
   * @return The clientHostname.
   */
  java.lang.String getClientHostname();
  /**
   * <code>string clientHostname = 1;</code>
   * @return The bytes for clientHostname.
   */
  com.google.protobuf.ByteString
      getClientHostnameBytes();

  /**
   * <code>int32 clientPort = 2;</code>
   * @return The clientPort.
   */
  int getClientPort();

  /**
   * <code>string filename = 3;</code>
   * @return The filename.
   */
  java.lang.String getFilename();
  /**
   * <code>string filename = 3;</code>
   * @return The bytes for filename.
   */
  com.google.protobuf.ByteString
      getFilenameBytes();

  /**
   * <code>string userID = 4;</code>
   * @return The userID.
   */
  java.lang.String getUserID();
  /**
   * <code>string userID = 4;</code>
   * @return The bytes for userID.
   */
  com.google.protobuf.ByteString
      getUserIDBytes();

  /**
   * <code>bool initial = 5;</code>
   * @return The initial.
   */
  boolean getInitial();

  /**
   * <code>string slave1Hostname = 6;</code>
   * @return The slave1Hostname.
   */
  java.lang.String getSlave1Hostname();
  /**
   * <code>string slave1Hostname = 6;</code>
   * @return The bytes for slave1Hostname.
   */
  com.google.protobuf.ByteString
      getSlave1HostnameBytes();

  /**
   * <code>int32 slave1Port = 7;</code>
   * @return The slave1Port.
   */
  int getSlave1Port();

  /**
   * <code>string slave2Hostname = 8;</code>
   * @return The slave2Hostname.
   */
  java.lang.String getSlave2Hostname();
  /**
   * <code>string slave2Hostname = 8;</code>
   * @return The bytes for slave2Hostname.
   */
  com.google.protobuf.ByteString
      getSlave2HostnameBytes();

  /**
   * <code>int32 slave2Port = 9;</code>
   * @return The slave2Port.
   */
  int getSlave2Port();
}
