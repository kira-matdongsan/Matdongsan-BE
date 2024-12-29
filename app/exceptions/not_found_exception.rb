class NotFoundException < CommonError

  def initialize(code: nil, message: nil, field: nil)
    super(status_code: :not_found, code: code, message: message || default_message, field: field)
  end

  private

  def default_message
    "Not Found"
  end
end
