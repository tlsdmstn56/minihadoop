// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: src/main/proto/cli.proto

package comnet.minihadoop.common.message.cli;

/**
 * Protobuf type {@code cli.LoginRequest}
 */
public  final class LoginRequest extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:cli.LoginRequest)
    LoginRequestOrBuilder {
private static final long serialVersionUID = 0L;
  // Use LoginRequest.newBuilder() to construct.
  private LoginRequest(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private LoginRequest() {
    iD_ = "";
    password_ = com.google.protobuf.ByteString.EMPTY;
  }

  @java.lang.Override
  @SuppressWarnings({"unused"})
  protected java.lang.Object newInstance(
      UnusedPrivateParameter unused) {
    return new LoginRequest();
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private LoginRequest(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    this();
    if (extensionRegistry == null) {
      throw new java.lang.NullPointerException();
    }
    com.google.protobuf.UnknownFieldSet.Builder unknownFields =
        com.google.protobuf.UnknownFieldSet.newBuilder();
    try {
      boolean done = false;
      while (!done) {
        int tag = input.readTag();
        switch (tag) {
          case 0:
            done = true;
            break;
          case 10: {
            java.lang.String s = input.readStringRequireUtf8();

            iD_ = s;
            break;
          }
          case 18: {

            password_ = input.readBytes();
            break;
          }
          default: {
            if (!parseUnknownField(
                input, unknownFields, extensionRegistry, tag)) {
              done = true;
            }
            break;
          }
        }
      }
    } catch (com.google.protobuf.InvalidProtocolBufferException e) {
      throw e.setUnfinishedMessage(this);
    } catch (java.io.IOException e) {
      throw new com.google.protobuf.InvalidProtocolBufferException(
          e).setUnfinishedMessage(this);
    } finally {
      this.unknownFields = unknownFields.build();
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return comnet.minihadoop.common.message.cli.Cli.internal_static_cli_LoginRequest_descriptor;
  }

  @java.lang.Override
  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return comnet.minihadoop.common.message.cli.Cli.internal_static_cli_LoginRequest_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            comnet.minihadoop.common.message.cli.LoginRequest.class, comnet.minihadoop.common.message.cli.LoginRequest.Builder.class);
  }

  public static final int ID_FIELD_NUMBER = 1;
  private volatile java.lang.Object iD_;
  /**
   * <code>string ID = 1;</code>
   * @return The iD.
   */
  public java.lang.String getID() {
    java.lang.Object ref = iD_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      iD_ = s;
      return s;
    }
  }
  /**
   * <code>string ID = 1;</code>
   * @return The bytes for iD.
   */
  public com.google.protobuf.ByteString
      getIDBytes() {
    java.lang.Object ref = iD_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      iD_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int PASSWORD_FIELD_NUMBER = 2;
  private com.google.protobuf.ByteString password_;
  /**
   * <code>bytes password = 2;</code>
   * @return The password.
   */
  public com.google.protobuf.ByteString getPassword() {
    return password_;
  }

  private byte memoizedIsInitialized = -1;
  @java.lang.Override
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  @java.lang.Override
  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    if (!getIDBytes().isEmpty()) {
      com.google.protobuf.GeneratedMessageV3.writeString(output, 1, iD_);
    }
    if (!password_.isEmpty()) {
      output.writeBytes(2, password_);
    }
    unknownFields.writeTo(output);
  }

  @java.lang.Override
  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (!getIDBytes().isEmpty()) {
      size += com.google.protobuf.GeneratedMessageV3.computeStringSize(1, iD_);
    }
    if (!password_.isEmpty()) {
      size += com.google.protobuf.CodedOutputStream
        .computeBytesSize(2, password_);
    }
    size += unknownFields.getSerializedSize();
    memoizedSize = size;
    return size;
  }

  @java.lang.Override
  public boolean equals(final java.lang.Object obj) {
    if (obj == this) {
     return true;
    }
    if (!(obj instanceof comnet.minihadoop.common.message.cli.LoginRequest)) {
      return super.equals(obj);
    }
    comnet.minihadoop.common.message.cli.LoginRequest other = (comnet.minihadoop.common.message.cli.LoginRequest) obj;

    if (!getID()
        .equals(other.getID())) return false;
    if (!getPassword()
        .equals(other.getPassword())) return false;
    return unknownFields.equals(other.unknownFields);
  }

  @java.lang.Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    hash = (37 * hash) + ID_FIELD_NUMBER;
    hash = (53 * hash) + getID().hashCode();
    hash = (37 * hash) + PASSWORD_FIELD_NUMBER;
    hash = (53 * hash) + getPassword().hashCode();
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static comnet.minihadoop.common.message.cli.LoginRequest parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static comnet.minihadoop.common.message.cli.LoginRequest parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static comnet.minihadoop.common.message.cli.LoginRequest parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static comnet.minihadoop.common.message.cli.LoginRequest parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static comnet.minihadoop.common.message.cli.LoginRequest parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static comnet.minihadoop.common.message.cli.LoginRequest parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static comnet.minihadoop.common.message.cli.LoginRequest parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static comnet.minihadoop.common.message.cli.LoginRequest parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static comnet.minihadoop.common.message.cli.LoginRequest parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static comnet.minihadoop.common.message.cli.LoginRequest parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static comnet.minihadoop.common.message.cli.LoginRequest parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static comnet.minihadoop.common.message.cli.LoginRequest parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  @java.lang.Override
  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(comnet.minihadoop.common.message.cli.LoginRequest prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
  @java.lang.Override
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE
        ? new Builder() : new Builder().mergeFrom(this);
  }

  @java.lang.Override
  protected Builder newBuilderForType(
      com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   * Protobuf type {@code cli.LoginRequest}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:cli.LoginRequest)
      comnet.minihadoop.common.message.cli.LoginRequestOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return comnet.minihadoop.common.message.cli.Cli.internal_static_cli_LoginRequest_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return comnet.minihadoop.common.message.cli.Cli.internal_static_cli_LoginRequest_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              comnet.minihadoop.common.message.cli.LoginRequest.class, comnet.minihadoop.common.message.cli.LoginRequest.Builder.class);
    }

    // Construct using comnet.minihadoop.common.message.cli.LoginRequest.newBuilder()
    private Builder() {
      maybeForceBuilderInitialization();
    }

    private Builder(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      super(parent);
      maybeForceBuilderInitialization();
    }
    private void maybeForceBuilderInitialization() {
      if (com.google.protobuf.GeneratedMessageV3
              .alwaysUseFieldBuilders) {
      }
    }
    @java.lang.Override
    public Builder clear() {
      super.clear();
      iD_ = "";

      password_ = com.google.protobuf.ByteString.EMPTY;

      return this;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return comnet.minihadoop.common.message.cli.Cli.internal_static_cli_LoginRequest_descriptor;
    }

    @java.lang.Override
    public comnet.minihadoop.common.message.cli.LoginRequest getDefaultInstanceForType() {
      return comnet.minihadoop.common.message.cli.LoginRequest.getDefaultInstance();
    }

    @java.lang.Override
    public comnet.minihadoop.common.message.cli.LoginRequest build() {
      comnet.minihadoop.common.message.cli.LoginRequest result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    @java.lang.Override
    public comnet.minihadoop.common.message.cli.LoginRequest buildPartial() {
      comnet.minihadoop.common.message.cli.LoginRequest result = new comnet.minihadoop.common.message.cli.LoginRequest(this);
      result.iD_ = iD_;
      result.password_ = password_;
      onBuilt();
      return result;
    }

    @java.lang.Override
    public Builder clone() {
      return super.clone();
    }
    @java.lang.Override
    public Builder setField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return super.setField(field, value);
    }
    @java.lang.Override
    public Builder clearField(
        com.google.protobuf.Descriptors.FieldDescriptor field) {
      return super.clearField(field);
    }
    @java.lang.Override
    public Builder clearOneof(
        com.google.protobuf.Descriptors.OneofDescriptor oneof) {
      return super.clearOneof(oneof);
    }
    @java.lang.Override
    public Builder setRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        int index, java.lang.Object value) {
      return super.setRepeatedField(field, index, value);
    }
    @java.lang.Override
    public Builder addRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return super.addRepeatedField(field, value);
    }
    @java.lang.Override
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof comnet.minihadoop.common.message.cli.LoginRequest) {
        return mergeFrom((comnet.minihadoop.common.message.cli.LoginRequest)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(comnet.minihadoop.common.message.cli.LoginRequest other) {
      if (other == comnet.minihadoop.common.message.cli.LoginRequest.getDefaultInstance()) return this;
      if (!other.getID().isEmpty()) {
        iD_ = other.iD_;
        onChanged();
      }
      if (other.getPassword() != com.google.protobuf.ByteString.EMPTY) {
        setPassword(other.getPassword());
      }
      this.mergeUnknownFields(other.unknownFields);
      onChanged();
      return this;
    }

    @java.lang.Override
    public final boolean isInitialized() {
      return true;
    }

    @java.lang.Override
    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      comnet.minihadoop.common.message.cli.LoginRequest parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (comnet.minihadoop.common.message.cli.LoginRequest) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }

    private java.lang.Object iD_ = "";
    /**
     * <code>string ID = 1;</code>
     * @return The iD.
     */
    public java.lang.String getID() {
      java.lang.Object ref = iD_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        iD_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <code>string ID = 1;</code>
     * @return The bytes for iD.
     */
    public com.google.protobuf.ByteString
        getIDBytes() {
      java.lang.Object ref = iD_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        iD_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>string ID = 1;</code>
     * @param value The iD to set.
     * @return This builder for chaining.
     */
    public Builder setID(
        java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      iD_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>string ID = 1;</code>
     * @return This builder for chaining.
     */
    public Builder clearID() {
      
      iD_ = getDefaultInstance().getID();
      onChanged();
      return this;
    }
    /**
     * <code>string ID = 1;</code>
     * @param value The bytes for iD to set.
     * @return This builder for chaining.
     */
    public Builder setIDBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      iD_ = value;
      onChanged();
      return this;
    }

    private com.google.protobuf.ByteString password_ = com.google.protobuf.ByteString.EMPTY;
    /**
     * <code>bytes password = 2;</code>
     * @return The password.
     */
    public com.google.protobuf.ByteString getPassword() {
      return password_;
    }
    /**
     * <code>bytes password = 2;</code>
     * @param value The password to set.
     * @return This builder for chaining.
     */
    public Builder setPassword(com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      password_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>bytes password = 2;</code>
     * @return This builder for chaining.
     */
    public Builder clearPassword() {
      
      password_ = getDefaultInstance().getPassword();
      onChanged();
      return this;
    }
    @java.lang.Override
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.setUnknownFields(unknownFields);
    }

    @java.lang.Override
    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.mergeUnknownFields(unknownFields);
    }


    // @@protoc_insertion_point(builder_scope:cli.LoginRequest)
  }

  // @@protoc_insertion_point(class_scope:cli.LoginRequest)
  private static final comnet.minihadoop.common.message.cli.LoginRequest DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new comnet.minihadoop.common.message.cli.LoginRequest();
  }

  public static comnet.minihadoop.common.message.cli.LoginRequest getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<LoginRequest>
      PARSER = new com.google.protobuf.AbstractParser<LoginRequest>() {
    @java.lang.Override
    public LoginRequest parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new LoginRequest(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<LoginRequest> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<LoginRequest> getParserForType() {
    return PARSER;
  }

  @java.lang.Override
  public comnet.minihadoop.common.message.cli.LoginRequest getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

