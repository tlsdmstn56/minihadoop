// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: src/main/proto/cli.proto

package comnet.minihadoop.common.message.cli;

public interface LoginResponseOrBuilder extends
    // @@protoc_insertion_point(interface_extends:cli.LoginResponse)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>bool success = 1;</code>
   * @return The success.
   */
  boolean getSuccess();

  /**
   * <code>.cli.LoginResponse.Type error = 2;</code>
   * @return The enum numeric value on the wire for error.
   */
  int getErrorValue();
  /**
   * <code>.cli.LoginResponse.Type error = 2;</code>
   * @return The error.
   */
  comnet.minihadoop.common.message.cli.LoginResponse.Type getError();
}
