module ApiResponse
  VALID_STATUSES = [:success, :fail, :error].freeze

  def render_json(status, status_code: nil, code: nil, message: nil, data: nil, field: nil, location: nil)
    validate_status!(status)

    status_code ||= get_status_code(status)
    code ||= get_code(status_code)

    dto = build_response_dto(
      status: status,
      code: code,
      message: message,
      data: data,
      field: field
    )

    render json: dto.as_json, status: status_code, location: location
  end

  private

  def validate_status!(status)
    raise StandardError.new("Invalid status: #{status}") unless VALID_STATUSES.include?(status)
  end

  def get_status_code(status)
    {
      success: :ok,
      fail: :bad_request,
      error: :internal_server_error
    }[status]
  end

  def get_code(symbol)
    Rack::Utils::SYMBOL_TO_STATUS_CODE[symbol]
  end

  def build_response_dto(status:, code:, message:, data:, field:)
    ApiResponseDto.new(
      status: status,
      code: code,
      message: message,
      error: status == :success ? nil : field,
      data: status == :success ? data : nil
    )
  end
end
