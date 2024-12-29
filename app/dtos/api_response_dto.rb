class ApiResponseDto
  attr_reader :status, :code, :message, :error, :data

  def initialize(status:, code:, message:, error:, data:)
    @status = status
    @code = code
    @message = message
    @error = error
    @data = format_data(data)
  end

  def format_data(data)
    case data
    when Array
      { contents: data }
    when ActiveRecord::Relation
      { contents: data.to_a }
    when TrueClass, FalseClass
      { result: data }
    else
      data
    end
  end

  def as_json
    {
      status: @status,
      code: @code,
      message: @message,
      error: @error,
      data: @data
    }
  end
end
