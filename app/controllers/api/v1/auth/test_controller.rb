class Api::V1::Auth::TestController < ApplicationController
  before_action :print_request
  before_action :authenticate_user!

  def print_request
    Rails.logger.info "Authorization Header: #{request.headers['Authorization']}"
  end

  def index
    @user = current_user
    render_json :success, data: @user
  end
end
