module ExceptionHandler
  extend ActiveSupport::Concern
  include ApiResponse

  included do
    rescue_from StandardError, with: :handle_standard_error
    rescue_from ActiveRecord::RecordNotFound, with: :handle_record_not_found
    rescue_from NotFoundException, with: :render_exception
  end

  def handle_record_not_found(exception)
    handle_exception(exception, :not_found)
  end

  def handle_standard_error(exception)
    handle_exception(exception, :internal_server_error)
  end

  def handle_exception(exception, status_code)
    log_error(exception)
    e = CommonError.from(exception, status_code)
    render_exception(e)
  end

  def render_exception(exception)
    status = exception.status_code == :internal_server_error ? :error : :fail

    render_json status,
                status_code: exception.status_code,
                code: exception.code,
                message: exception.message,
                field: exception.field
  end

  def log_error(exception)
    logger.error(exception.full_message)
  end
end
