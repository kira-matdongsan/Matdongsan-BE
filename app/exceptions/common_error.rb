class CommonError < StandardError
  attr_reader :status_code, :code, :message, :field

  def initialize(status_code:, code: nil, message:, field: nil)
    @status_code = status_code
    @code = code
    @message = message
    @field = field
  end

  def self.from(exception, status_code)
    new(
      status_code: status_code,
      message: exception.message
    )
  end
end
