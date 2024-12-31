class Api::V1::Auth::SessionsController < Devise::SessionsController
  include ApiResponse
  include ExceptionHandler
  respond_to :json

  private

  def respond_with(resource, _opts = {})
    render_json :success, data: resource
  end
end
